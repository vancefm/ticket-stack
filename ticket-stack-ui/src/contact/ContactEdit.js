import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label, Row, Col} from 'reactstrap';
import AppNavbar from '../AppNavbar';
import {checkForResponseErrors} from "../utils/ErrorUtil";
import { useAlert } from '../utils/AlertProvider'


const ContactEdit = () => {
    const initialContactFormState = {
        id: '',
        emailAddress: '',
        firstName: '',
        lastName: ''
    };

    const [contact, setContact] = useState(initialContactFormState);
    const navigate = useNavigate();
    const { id } = useParams();
    const [open, close] = useAlert();

    useEffect(() => {

        //Get existing contact
        if (id !== 'new') {
            fetch(`/contact/${id}`, {method : 'get'})
                .then(response => response.json())
                .then(contactData => setContact(contactData));
        }

    }, [id, setContact]);

    const handleChange = (event) => {
        const { name, value } = event.target

        setContact({ ...contact, [name]: value });
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        let requestMethod = (contact.id) ? 'PUT' : 'POST'
        let contactJSON = JSON.stringify(contact)

        const response = await fetch('/contact/' + (contact.id ? '/' + contact.id : ''), {
            method: requestMethod,
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
            body: contactJSON
        });

        var responseData = await response.json();

        checkForResponseErrors(requestMethod, contactJSON, responseData)
        if(response.status !== 201){
            open(responseData["errors"]);
        }

        setContact(initialContactFormState);
        navigate('/contact');
    }

    const title = contact.id ? <h2>Edit Contact {contact.id}</h2> : <h2>Create Contact</h2>;

    return (<div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={handleSubmit}>
                    <Row>
                        <Col md={1}>
                            <FormGroup>
                                <Label for="contactId">Contact ID</Label>
                                <Input type="text" name="contactId" id="contactId" value={contact.id || ''}
                                       autoComplete="contactId" readOnly disabled/>
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row>
                        <Col md={3}>
                            <FormGroup>
                                <Label for="emailAddress">Email*</Label>
                                <Input type="text" name="emailAddress" id="emailAddress" value={contact.emailAddress || ''}
                                       onChange={handleChange} autoComplete="emailAddress"/>
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row>
                        <Col md={2}>
                            <FormGroup>
                            <Label for="firstName">First Name</Label>
                            <Input type="text" name="firstName" id="firstName" value={contact.firstName || ''}
                                   onChange={handleChange} autoComplete="firstName"/>
                            </FormGroup>
                        </Col>
                        <Col md={2}>
                            <FormGroup>
                                <Label for="lastName">Last Name</Label>
                                <Input type="text" name="lastName" id="lastName" value={contact.lastName || ''}
                                       onChange={handleChange} autoComplete="lastName"/>
                            </FormGroup>
                        </Col>
                    </Row>

                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/contact">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    )
};

export default ContactEdit;