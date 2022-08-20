package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.pojos.Contact;
import com.vancefm.ticketstack.models.tables.records.ContactRecord;
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

import static com.vancefm.ticketstack.models.tables.Contact.CONTACT;

@Service
public class ContactService implements BasicService<Contact>{

    @Autowired
    DSLContext context;

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
    public void createOrUpdate(Contact contact) {
        ContactRecord contactRecord = context.fetchOne(CONTACT, CONTACT.EMAIL_ADDRESS.eq(contact.getEmailAddress()));
        if (contactRecord == null) {
            contactRecord = context.newRecord(CONTACT);
            modelMapper.map(contact, contactRecord);
        }

        try {
            context.loadInto(CONTACT)
                    .onDuplicateKeyUpdate()
                    .loadRecords(contactRecord)
                    .fieldsCorresponding()
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        ContactRecord contactRecord = context.fetchOne(CONTACT, CONTACT.ID.eq(id));
        if (contactRecord != null) {
            contactRecord.delete();
        }
    }
}
