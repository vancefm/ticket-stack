import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from '../AppNavbar';
import { Link } from 'react-router-dom';

const ContactList = () => {

    const [contacts, setContacts] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);

        fetch('/contact')
            .then(response => response.json())
            .then(data => {
                setContacts(data);
                setLoading(false);
            })
    }, []);

    const remove = async (id) => {
        await fetch(`/contact/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedContacts = [...contacts].filter(i => i.id !== id);
            setContacts(updatedContacts);
        });
    }

    if (loading) {
        return <p>Loading...</p>;
    }

    const contactList = contacts.map(contact => {
        return <tr key={contact.id}>
            <td>{contact.id}</td>
            <td>{contact.emailAddress}</td>
            <td>{contact.firstName}</td>
            <td>{contact.lastName}</td>
            <td>
                <ButtonGroup>
                    <Button size="sm" color="primary" tag={Link} to={"/contact/" + contact.id}>Edit</Button>
                    <Button size="sm" color="danger" onClick={() => remove(contact.id)}>Delete</Button>
                </ButtonGroup>
            </td>
        </tr>
    });

    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <div className="float-end">
                    <Button color="success" tag={Link} to="/contact/new">Create Contact</Button>
                </div>
                <h3>Contact</h3>
                <Table className="mt-4">
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Email Address</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {contactList}
                    </tbody>
                </Table>
            </Container>
        </div>
    );
};

export default ContactList;