package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.models.tables.records.TicketRecord;
import com.vancefm.ticketstack.pojos.Ticket;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.vancefm.ticketstack.models.tables.Ticket.TICKET;

@Service
public class TicketService implements BasicService<Ticket>{

    private final DSLContext context;

    private final ModelMapper modelMapper = new ModelMapper();

    TicketService(DSLContext context){
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        this.context = context;
    }

    public List<Ticket> getAll(){

        //Note mysql timestamp datatype converts values to UTC for storing, and from UTC when retrieving
        //So depending on the database server timezone, the time values may be different than expected.

        return context
                .select()
                .from(TICKET)
                .fetchInto(Ticket.class);
    }

    public Ticket getByID(Integer id){
        //Note mysql timestamp datatype converts values to UTC for storing, and from UTC when retrieving
        //So depending on the database server timezone, the time values may be different than expected.
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

        TicketRecord ticketRecord = null;
        if (ticket != null) {
            ticketRecord = context.newRecord(TICKET);
            ticket.setCreatedTime(LocalDateTime.now());
            ticket.setUpdatedTime(LocalDateTime.now());
            modelMapper.map(ticket, ticketRecord);
            ticketRecord.store();
            ticketRecord = context.fetchOne(TICKET, TICKET.ID.eq(ticketRecord.getId()));
        }

        //ticket will have an ID, and may have other generated fields that we can return, so lets map it all back
        modelMapper.map(ticketRecord, ticket);
        return ticket;
    }

    public Ticket update(Integer id, Ticket ticket){
        TicketRecord ticketRecord = context.fetchOne(TICKET, TICKET.ID.eq(id));
        if (ticketRecord != null) {
            ticket.setUpdatedTime(LocalDateTime.now());
            modelMapper.map(ticket, ticketRecord);
            ticketRecord.store();
        }
        return ticket;
    }

    public Ticket delete(Integer id){
        TicketRecord ticketRecord = context.fetchOne(TICKET, TICKET.ID.eq(id));
        Ticket ticket = null;
        if (ticketRecord != null) {
            ticket = new Ticket();
            modelMapper.map(ticketRecord, ticket);
            ticketRecord.delete();
            return ticket;
        }
        return null;
    }
}
