package com.ticket.monolithticketmonster;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
@Order(1)
public class UseCaseLoggingAdvice {

  @Pointcut("within(@com.ticket.monolithticketmonster.UseCase *)")
  public void useCase() {
  }

  @Pointcut("execution(public * *(..))")
  public void publicMethod() {
  }

  @Pointcut("publicMethod() && useCase()")
  public void publicMethodInsideAUseCase() {
  }

  @Around("publicMethodInsideAUseCase()")
  public Object aroundServiceMethodAdvice(final ProceedingJoinPoint pjp) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    try {
      log.info("Executing use case: {}#{} with parameters: {}", pjp.getTarget().getClass(), pjp.getSignature().getName(), Arrays.toString(pjp.getArgs()));
      stopWatch.start();
      return pjp.proceed();
    } finally {
      stopWatch.stop();
      log.info("Finished executing use case {}#{} in {}ms", pjp.getTarget().getClass(), pjp.getSignature().getName(), stopWatch.getTotalTimeMillis());
    }
  }
}
