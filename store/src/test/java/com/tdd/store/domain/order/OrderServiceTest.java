package com.tdd.store.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.tdd.store.domain.payment.PaymentService;
import com.tdd.store.domain.user.UserService;

public class OrderServiceTest {
  
  private PaymentService paymentService = mock(PaymentService.class);
  private UserService userService = mock(UserService.class);
  private OrderService orderService = new OrderService(userService, paymentService);
  
  @Test
  public void should_throw_an_exception_when_user_is_minor() {

    when(userService.isUserMinor(1L)).thenReturn(true);
    doNothing().when(paymentService).pay();

    Order order = new Order(1L);
    
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.create(order));
    assertEquals("O usuário não pode ser menor de idade", exception.getMessage());

    verify(userService, times(1)).isUserMinor(1L);
    verify(paymentService, times(0)).pay();
  }

  @Test
  public void should_pay_when_the_user_is_of_legal_age() {

    when(userService.isUserMinor(2L)).thenReturn(false);
    doNothing().when(paymentService).pay();

    Order order = new Order(2L);
    
    orderService.create(order);

    verify(userService, times(1)).isUserMinor(2L);
    verify(paymentService, times(1)).pay();
  }
}
