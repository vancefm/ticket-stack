import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label, Row} from 'reactstrap';
import AppNavbar from '../AppNavbar';
import {checkForResponseErrors} from "../utils/ErrorUtil";
import { useAlert } from '../utils/AlertProvider'


const TicketEdit = () => {
     const initialTicketFormState = {
         id: '',
         subject: '',
         contactId: '',
         requestCategoryId: '',
         description: '',
         statusId: '',
         createdTime:'',
         updatedTime:'',
         closedTime:''
     };

    const [ticket, setTicket] = useState(initialTicketFormState);
    const navigate = useNavigate();
    const { id } = useParams();
    const [open, close] = useAlert();

    //On page load, get the info for the ticket we're working on
    useEffect(() => {
        //Get existing ticket
        if (id !== 'new') {
            fetch(`/ticket/${id}`, {method : 'get'})
                .then(response => response.json())
                .then(ticketData => setTicket(ticketData));
        }

    }, [id, setTicket]);

    //When a ticket is edited, set the new values
    const handleChange = (event) => {
        const { name, value } = event.target
        ticket.statusId=null
        ticket.contactId=null
        setTicket({ ...ticket, [name]: value })
    }

    //Submit the updated ticket info
    const handleSubmit = async (event) => {
        event.preventDefault();

        var requestMethod = (ticket.id) ? 'PUT' : 'POST'
        var ticketJSON = JSON.stringify(ticket)

        const response = await fetch('/ticket/' + (ticket.id ? '/' + ticket.id : ''), {
            method: requestMethod,
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
            body: ticketJSON
        });

        var responseData = await response.json();

        checkForResponseErrors(requestMethod, ticketJSON, responseData)
        if(response.status !== 201){
            open(responseData["errors"]);
        }

        setTicket(initialTicketFormState);
        navigate('/ticket');
    }

    const title = ticket.id ? <h2>Edit Ticket {ticket.id}</h2> : <h2>Create Ticket</h2>;


    const closeTicket = async() => {
        console.log(ticket.closedTime)
        let requestMethod = 'PUT'
        let ticketJSON = JSON.stringify(ticket)
        const response = await fetch('/ticket/set-closed/' + (ticket.id), {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: ticketJSON
        });

        checkForResponseErrors(requestMethod, ticketJSON, response)

        setTicket(initialTicketFormState);
        navigate('/ticket');
    }

    return (<div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={handleSubmit}>
                    <Row>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="ticketId">Ticket ID</Label>
                            <Input type="text" name="ticketId" id="ticketId" value={ticket.id || ''}
                                   onChange={handleChange} autoComplete="ticketId" readOnly disabled/>
                        </FormGroup>
                        <FormGroup className="col-md-2 mb-3">
                            <Label for="statusId">Status ID*</Label>
                            <Input type="text" name="statusId" id="statusId" value={ticket.statusId || ''}
                                   onChange={handleChange} autoComplete="statusId"/>
                        </FormGroup>
                    </Row>

                    <FormGroup className="col-md-5 mb-3">
                        <Label for="contact">Contact ID*</Label>
                        <Input type="text" name="contactId" id="contactId" defaultValue={ticket.contactId}
                               onChange={handleChange} autoComplete="contactId"/>
                    </FormGroup>
                    <FormGroup className="col-md-2 mb-3">
                        <Label for="requestCategoryId">Category*</Label>
                        <Input type="text" name="requestCategoryId" id="requestCategoryId" defaultValue={ticket.requestCategoryId}
                               onChange={handleChange} autoComplete="requestCategoryId"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="subject">Subject*</Label>
                        <Input type="text" name="subject" id="subject" value={ticket.subject || ''}
                               onChange={handleChange} autoComplete="subject"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="description">Description</Label>
                        <Input type="textarea" name="description" id="description" value={ticket.description || ''}
                               onChange={handleChange} autoComplete="description"/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/ticket">Cancel</Button>{' '}
                        <Button color="success" onClick={closeTicket}>Close Ticket</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    )
};

export default TicketEdit;