package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.pojos.Ticket;
import com.vancefm.ticketstack.models.tables.records.TicketRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.vancefm.ticketstack.models.tables.Ticket.TICKET;

@Service
public class TicketService implements BasicService<Ticket>{

    @Autowired
    DSLContext context;

    private final ModelMapper modelMapper = new ModelMapper();

    TicketService(){
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
    }

    public List<Ticket> getAll(){

        return context
                .select()
                .from(TICKET)
                .fetchInto(Ticket.class);
    }

    public Ticket getByID(Integer id){
        Result<Record> record = context
                .select()
                .from(TICKET)
                .where(TICKET.ID.eq(id))
                .fetch();

        modelMapper.getConfiguration().addValueReader(new RecordValueReader());

        if(record.size() > 0){
            return modelMapper.map(record.get(0), Ticket.class);
        } else {
            return null;
        }
    }



    public Ticket create(Ticket ticket){
        TicketRecord ticketRecord = context.fetchOne(TICKET, TICKET.ID.eq(ticket.getId()));
        if (ticketRecord == null) {
            ticketRecord = context.newRecord(TICKET);
            modelMapper.map(ticket, ticketRecord);
            ticketRecord.store();
            modelMapper.map(ticketRecord, ticket);
        }
        modelMapper.map(ticketRecord, ticket);
        return ticket;
    }

    public Ticket update(Ticket ticket){
        TicketRecord ticketRecord = context.fetchOne(TICKET, TICKET.ID.eq(ticket.getId()));
        if (ticketRecord != null) {
            modelMapper.map(ticket, ticketRecord);
            ticketRecord.store();
        }
        return null;
    }

    public void delete(Integer id){
        TicketRecord ticketRecord = context.fetchOne(TICKET, TICKET.ID.eq(id));
        if (ticketRecord != null) {
            ticketRecord.delete();
        }
    }
}
