import React from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import TicketList from './ticket/TicketList';
import TicketEdit from "./ticket/TicketEdit";
import ContactList from "./contact/ContactList";
import ContactEdit from "./contact/ContactEdit";
import {AlertProvider} from "./utils/AlertProvider";

const App = () => {
    return (
        <AlertProvider>
            <Router>
                <Routes>
                    <Route exact path="/" element={<Home/>}/>
                    <Route path='/ticket' exact={true} element={<TicketList/>}/>
                    <Route path='/ticket/:id' exact={true} element={<TicketEdit/>}/>
                    <Route path='/contact' exact={true} element={<ContactList/>}/>
                    <Route path='/contact/:id' exact={true} element={<ContactEdit/>}/>
                </Routes>
            </Router>
        </AlertProvider>
    )
}

export default App;