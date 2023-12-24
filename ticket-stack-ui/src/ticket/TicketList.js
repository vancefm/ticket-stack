import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from '../AppNavbar';
import { Link } from 'react-router-dom';

const TicketList = () => {

    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(false);

    //On page load, get the full ticket list
    useEffect(() => {
        setLoading(true);

        fetch('/ticket')
            .then(response => response.json())
            .then(data => {
                setTickets(data);
                setLoading(false);
            })
    }, []);

    //Remove a ticket by ID
    const remove = async (id) => {
        await fetch(`/ticket/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedTickets = [...tickets].filter(i => i.id !== id);
            setTickets(updatedTickets);
        });
    }

    //Display a loading page as needed
    if (loading) {
        return <p>Loading...</p>;
    }

    //Populate the ticket list
    const ticketList = tickets.map(ticket => {
        return <tr key={ticket.id}>
            <td>{ticket.id}</td>
            <td style={{whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden', maxWidth: '1px'}}>{ticket.subject}</td>
            <td style={{whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden', maxWidth: '1px'}}>{ticket.description}</td>
            <td>{ticket.contactId}</td>
            <td>{ticket.createdTime}</td>
            <td>{ticket.updatedTime}</td>
            <td>{ticket.closedTime}</td>
            <td>
                <ButtonGroup>
                    <Button size="sm" color="primary" tag={Link} to={"/ticket/" + ticket.id}>Edit</Button>
                    <Button size="sm" color="danger" onClick={() => remove(ticket.id)}>Delete</Button>
                </ButtonGroup>
            </td>
        </tr>
    });

    //Display the ticket list
    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <div className="float-end">
                    <Button color="success" tag={Link} to="/ticket/new">Create Ticket</Button>
                </div>
                <h3>Tickets</h3>
                <Table className="mt-4">
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th width="10%">Subject</th>
                        <th width="15%">Description</th>
                        <th>Contact</th>
                        <th>Created Time (UTC)</th>
                        <th>Updated Time</th>
                        <th>Closed Time</th>
                        <th width="10%">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {ticketList}
                    </tbody>
                </Table>
            </Container>
        </div>
    );
};

export default TicketList;