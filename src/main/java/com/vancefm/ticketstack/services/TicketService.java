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
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;

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

        //See if a record exists
        TicketRecord ticketRecord = context.fetchOne(TICKET, TICKET.ID.eq(ticket.getId()));
        if (ticketRecord == null) {

            //Record wasn't found, so let's create one
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

    public Ticket update(Ticket ticket){
        TicketRecord ticketRecord = context.fetchOne(TICKET, TICKET.ID.eq(ticket.getId()));
        if (ticketRecord != null) {
            ticket.setUpdatedTime(LocalDateTime.now());
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
