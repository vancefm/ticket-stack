import React from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
/*import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';*/

const Home = () => {
    return (
        <div>
            <AppNavbar/>
            {/*<Container fluid>
                <Button color="link"><Link to="/ticket">Tickets</Link></Button>
            </Container>*/}
            Home Page things here.
        </div>
    );
}

export default Home;