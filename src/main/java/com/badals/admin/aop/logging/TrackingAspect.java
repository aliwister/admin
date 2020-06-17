package com.badals.admin.aop.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class TrackingAspect {

/*   private final TenantRepository tenantRepository;

   @Autowired
   public TenantAspect(TenantRepository tenantRepository) {
      this.tenantRepository = tenantRepository;
   }*/

   //@Around(value = "execution(* com.badals.admin.service.mutation.ShipmentMutation.*(..)) || execution(* com.badals.admin.service.query.ShipmentQuery.*(..))")
   public Object assignForController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      return assignTenant(proceedingJoinPoint);
   }

   private Object assignTenant(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      try {
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         System.out.println("ASPECTJ " + user.getUsername());
         if (user == null) {
            throw new IllegalAccessException("Not Authorized");
         }
      } finally {
         Object retVal;
         retVal = proceedingJoinPoint.proceed();
         return retVal;
      }
   }
}
