package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.models.tables.records.ContactRecord;
import com.vancefm.ticketstack.pojos.Contact;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.vancefm.ticketstack.models.tables.Contact.CONTACT;

@Service
public class ContactService implements BasicService<Contact>{

    private DSLContext context;

    public ContactService(DSLContext context) {
        this.context = context;
    }

    private final ModelMapper modelMapper = new ModelMapper();

    ContactService() { modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);}

    @Override
    public List<Contact> getAll() {
        return context
                .select()
                .from(CONTACT)
                .fetchInto(Contact.class);
    }

    @Override
    public Contact getByID(Integer id) {
        Result<Record> record = context
                .select()
                .from(CONTACT)
                .where(CONTACT.ID.eq(id))
                .fetch();

        modelMapper.getConfiguration().addValueReader(new RecordValueReader());

        if(record.size() > 0){
            return modelMapper.map(record.get(0), Contact.class);
        } else {
            return null;
        }
    }

    @Override
    public Contact create(Contact contact) {
        ContactRecord contactRecord = context.fetchOne(CONTACT, CONTACT.EMAIL_ADDRESS.eq(contact.getEmailAddress()));
        if (contactRecord == null) {
            contactRecord = context.newRecord(CONTACT);
            modelMapper.map(contact, contactRecord);
            contactRecord.store();
        }
        //contact will have an ID, and may have other generated fields that we can return, so lets map it all back
        modelMapper.map(contactRecord, contact);
        return contact;

    }

    @Override
    public Contact update(Integer id, Contact contact) {
        ContactRecord contactRecord = context.fetchOne(CONTACT, CONTACT.EMAIL_ADDRESS.eq(contact.getEmailAddress()));
        if (contactRecord != null) {
            modelMapper.map(contact, contactRecord);
            contactRecord.store();
        }
        return null;
    }

    @Override
    public Contact delete(Integer id) {
        ContactRecord contactRecord = context.fetchOne(CONTACT, CONTACT.ID.eq(id));
        Contact contact = null;
        if (contactRecord != null) {
            contactRecord.delete();
            modelMapper.map(contactRecord, contact);
            return contact;
        }
        return null;
    }
}
