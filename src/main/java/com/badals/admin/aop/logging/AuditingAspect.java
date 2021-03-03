package com.badals.admin.aop.logging;

import com.badals.admin.domain.Action;
import com.badals.admin.repository.ActionRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class AuditingAspect {

/*   private final TenantRepository tenantRepository;

   @Autowired
   public TenantAspect(TenantRepository tenantRepository) {
      this.tenantRepository = tenantRepository;
   }*/
   @Autowired
   ActionRepository actionRepository;

   @Around("execution(* com.badals.admin.service.mutation.ShipmentMutation.*(..))")
   //|| execution(* com.badals.admin.service.query.ShipmentQuery.*(..))")
   public Object beforeWebMethodExecution1(ProceedingJoinPoint joinPoint) throws Throwable {
      addAction(joinPoint);
      return joinPoint.proceed();
   }

   private void addAction(ProceedingJoinPoint joinPoint) {
      Object[] args = joinPoint.getArgs();
      String methodName = joinPoint.getSignature().getName();
      String principal =  Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
      String className = joinPoint.getSignature().getDeclaringTypeName();
      Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
      // only log those methods called by an end user


      System.out.println(methodName);
      CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

      int i = 0;
      Action action = new Action();
      action.setAction(methodName);
      action.setObject(getObject(className));

      if(principal != null)
         action.setCreatedBy(principal);



      String state = "";
      for(Object o : args) {
         String key, value;
         key = codeSignature.getParameterNames()[i];
         value = joinPoint == null || joinPoint.getArgs() == null || joinPoint.getArgs()[i] == null?"":joinPoint.getArgs()[i].toString();
         System.out.println("First parameter's name: " + key);
         System.out.println("First argument's value: " + value);

/*            if (key.equalsIgnoreCase("status") || key.equalsIgnoreCase("state"))
                action.setState(value);*/
         if (key.equalsIgnoreCase("comment"))
            action.setComment(value);
         if (action.getObjectId() == null && (key.toLowerCase().endsWith("id") || key.toLowerCase().endsWith("key") || key.toLowerCase().endsWith("slug") || key.toLowerCase().endsWith("sku") || key.toLowerCase().endsWith("dial")) )
            action.setObjectId(value);
         else
            state += " "+key+ "="+value;
         i++;
      }
      action.setState(state);
      actionRepository.save(action);
   }

   private String getObject(String className) {

      return "shipment";
   }
}
