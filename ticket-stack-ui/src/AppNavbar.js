import React, { useState } from 'react';
import { Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem, NavLink } from 'reactstrap';
import { Link } from 'react-router-dom';

const AppNavbar = () => {

    const [isOpen, setIsOpen] = useState(false);

    return (
        <Navbar color="dark" dark expand="md">
            <NavbarBrand tag={Link} to="/">Ticket Stack</NavbarBrand>
            <NavbarToggler onClick={() => { setIsOpen(!isOpen) }}/>
            <Collapse isOpen={isOpen} navbar>
                <Nav className="justify-content-front" style={{width: "85%"}}>
                    <NavItem>
                        <NavLink tag={Link} to="/ticket">Tickets</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink tag={Link} to="/contact">Contacts</NavLink>
                    </NavItem>
                </Nav>
                <Nav className="justify-content-end" navbar>
                    <NavItem>
                        <NavLink href="https://twitter.com/vancefm">@vancefm</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink target="_blank" href="https://github.com/vancefm">GitHub</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink target="_blank" href="http://localhost:8080/swagger-ui/index.html">Swagger Docs</NavLink>
                    </NavItem>
                </Nav>
            </Collapse>
        </Navbar>
    );
};

export default AppNavbar;