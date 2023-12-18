package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.entities.TicketStatus;
import com.vancefm.ticketstack.repositories.TicketStatusRepository;
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
public class TicketStatusServiceTest {
    
    @Mock
    TicketStatusRepository ticketStatusRepository;
    
    @InjectMocks
    TicketStatusService ticketStatusService;

    @Test
    public void shouldGetAllTicketStatuss_andReturnTicketStatusList(){
        TicketStatus statusOne = new TicketStatus(1, "Status 1");
        TicketStatus statusTwo = new TicketStatus(2, "Status 2");
        TicketStatus statusThree = new TicketStatus(3, "Status 3");

        when(ticketStatusRepository.findAll()).thenReturn(Arrays.asList(statusOne,statusTwo,statusThree));

        Optional<List<TicketStatus>> resultList = ticketStatusService.getAll();
        resultList.ifPresentOrElse(list -> {
                    assertFalse(list.isEmpty());
                    assertEquals(3, list.size());
                    list.forEach(ticketStatus -> System.out.println(ticketStatus.getStatusName()));
                },
                Assertions::fail
        );
    }

    @Test
    public void shouldGetATicketStatusById_andReturnATicketStatus(){
        TicketStatus statusOne = new TicketStatus(1, "Status 1");

        when(ticketStatusRepository.findById(statusOne.getId())).thenReturn(Optional.of(statusOne));

        Optional<TicketStatus> resultTicketStatus = ticketStatusService.getByID(statusOne.getId());
        resultTicketStatus.ifPresentOrElse(ticketStatus ->
                        System.out.println(ticketStatus.getStatusName()),
                Assertions::fail
        );
    }

    @Test
    public void shouldCreateATicketStatus_andReturnTheSameTicketStatusWithAnID(){
        TicketStatus statusOne = new TicketStatus(null, "Status 1");
        TicketStatus statusTwo = new TicketStatus(1, "Status 1");

        when(ticketStatusRepository.save(statusOne)).thenReturn(statusTwo);

        Optional<TicketStatus> resultTicketStatus = ticketStatusService.create(statusOne);

        resultTicketStatus.ifPresentOrElse(ticketStatus ->
                {
                    if(ticketStatus.getStatusName().equals(statusTwo.getStatusName())
                            && ticketStatus.getId() != null) {
                        System.out.println(ticketStatus.getStatusName());
                    } else {
                        Assertions.fail();
                    }
                },
                Assertions::fail
        );
    }

    @Test
    public void shouldDeleteATicketStatus_andReturnNothing(){
        TicketStatus statusOne = new TicketStatus(1, "Status 1");

        assertDoesNotThrow(() -> ticketStatusRepository.deleteById(statusOne.getId()));
    }
    
}
