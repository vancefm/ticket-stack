package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.entities.Ticket;
import com.vancefm.ticketstack.repositories.TicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TicketServiceTest {

    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    TicketService ticketService;

    @Test
    public void shouldGetAllTickets_andReturnTicketList(){
        Ticket ticketOne = new Ticket(1,"Test ticket 1",1,1,"",1,null,null,null);
        Ticket ticketTwo = new Ticket(2,"Test ticket 2",1,1,"",1,null,null,null);
        Ticket ticketThree = new Ticket(3,"Test ticket 3",1,1,"",1,null,null,null);

        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticketOne, ticketTwo, ticketThree));

        Optional<List<Ticket>> resultList = ticketService.getAll();
        resultList.ifPresentOrElse(list -> {
                    assertFalse(list.isEmpty());
                    assertEquals(3, list.size());
                    list.forEach(ticket -> System.out.println(ticket.getSubject()));
                },
                Assertions::fail
        );
    }

    @Test
    public void shouldGetATicketByID_andReturnATicket(){
        Ticket ticketOne = new Ticket(1,"Test ticket 1",1,1,"",1,null,null,null);

        when(ticketRepository.findById(ticketOne.getId())).thenReturn(Optional.of(ticketOne));

        Optional<Ticket> resultTicket = ticketService.getByID(ticketOne.getId());
        resultTicket.ifPresentOrElse(ticket ->
                System.out.println(ticket.getSubject()),
                Assertions::fail
        );
    }

    @Test
    public void shouldCreateATicket_andReturnTheSameTicketWithAnID(){
        Ticket ticketOne = new Ticket(null,"Test ticket 1",1,1,"",1,null,null,null);
        Ticket ticketTwo = new Ticket(1,"Test ticket 1",1,1,"",1,null,null,null);

        when(ticketRepository.save(ticketOne)).thenReturn(ticketTwo);

        Optional<Ticket> resultTicket = ticketService.create(ticketOne);
        resultTicket.ifPresentOrElse(ticket ->
                {
                    if(ticket.getSubject().equals(ticketTwo.getSubject())
                        && ticket.getId() != null) {
                        System.out.println(ticket.getSubject());
                    }
                },
                Assertions::fail
        );
    }

    @Test
    public void shouldDeleteAContact_andReturnNothing(){
        Ticket ticketOne = new Ticket(1,"Test ticket 1",1,1,"",1,null,null,null);

        assertDoesNotThrow(() -> ticketRepository.deleteById(ticketOne.getId()));
    }
}
