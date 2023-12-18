package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.entities.RequestCategory;
import com.vancefm.ticketstack.repositories.RequestCategoryRepository;
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
public class RequestCategoryServiceTest {

    @Mock
    RequestCategoryRepository requestCategoryRepository;

    @InjectMocks
    RequestCategoryService requestCategoryService;

    @Test
    void shouldGetAllRequestCategories_andReturnACateogryList(){
        RequestCategory categoryOne = new RequestCategory(1, "Category 1");
        RequestCategory categoryTwo = new RequestCategory(2, "Category 2");
        RequestCategory categoryThree = new RequestCategory(3, "Category 3");

        when(requestCategoryRepository.findAll()).thenReturn(Arrays.asList(categoryOne,categoryTwo,categoryThree));

        Optional<List<RequestCategory>> categoryList = requestCategoryService.getAll();
        categoryList.ifPresentOrElse(list -> {
            assertFalse(list.isEmpty());
            assertEquals(3, list.size());
            list.forEach(category -> System.out.println(category.getCategoryName()));
        },
                Assertions::fail
        );
    }

    @Test
    void shouldGetARequestCategoryByID_andReturnACategory(){
        RequestCategory categoryOne = new RequestCategory(1, "Category 1");

        when(requestCategoryRepository.findById(categoryOne.getId())).thenReturn(Optional.of(categoryOne));

        Optional<RequestCategory> category = requestCategoryService.getByID(categoryOne.getId());
        category.ifPresentOrElse(c ->
            System.out.println(c.getCategoryName())
        ,
                Assertions::fail
        );
    }

    @Test
    void shouldCreateARequestCategory_andReturnTheSameCategoryWithAnID(){
        RequestCategory categorytest = new RequestCategory(null, "Category Test");
        RequestCategory categorytestresult = new RequestCategory(1, "Category Test");

        when(requestCategoryRepository.save(categorytest)).thenReturn(categorytestresult);

        Optional<RequestCategory> result = requestCategoryService.create(categorytest);
        result.ifPresentOrElse(c -> {
                    if(c.getCategoryName().equals(categorytestresult.getCategoryName())
                            && c.getId() != null) {
                        System.out.println(c.getCategoryName());
                    } else {
                        Assertions.fail();
                    }
        },
                Assertions::fail
        );

    }

    @Test
    void shouldDeleteARequestCategory_andReturnNothing(){
        RequestCategory categorytest = new RequestCategory(1, "Category Test");

        assertDoesNotThrow(() -> requestCategoryService.delete(categorytest.getId()));
    }
}
