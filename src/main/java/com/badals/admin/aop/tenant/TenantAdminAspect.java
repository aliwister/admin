package com.badals.admin.aop.tenant;

import com.badals.admin.aop.logging.TenantContext;

import com.badals.admin.security.jwt.ProfileUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class TenantAdminAspect {
   @PersistenceContext
   private EntityManager entityManager;

//   private final TenantRepository tenantRepository;
//
//   @Autowired
//   public TenantAdminAspect(TenantRepository tenantRepository) {
//      this.tenantRepository = tenantRepository;
//   }
    public TenantAdminAspect() {
    }

   /**
    * Ensures the current user has sufficient access to the tenant.
    * @param proceedingJoinPoint
    * @return
    * @throws Throwable
    */
   @Around(value = "execution(* com.badals.admin.service.ShipmentReceiptService.*(..)) " +
       "|| execution(* com.badals.admin.service.ShipmentItemService.*(..)) " +
       "|| execution(* com.badals.admin.service.ShipmentService.*(..)) " +
       "|| execution(* com.badals.admin.service.ItemIssuanceService.*(..)) " +
       "|| execution(* com.badals.admin.service.TrackingService.*(..))")
   public Object assignForController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      ProfileUser userObj =  (ProfileUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (userObj == null|| userObj.equals("anonymousUser")) {
         throw new IllegalAccessException("Not Authorized");
      }
      // Get the tenant the user is logged in for (done using select-store)
      String tenant = userObj.getTenantId() == null ? userObj.getProfile() : userObj.getTenantId();

      if (tenant != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
         filter.setParameter("tenantId", tenant);
         filter.validate();
      } else {
         throw new IllegalAccessException("Not Authorized");
      }
      return assignTenant(proceedingJoinPoint, tenant);
   }
/*

   @Around(value = "execution(* com.badals.shop.graph.mutation.TenantMutation.*(..)) ")
   public Object assignForController2(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      ProfileUser userObj =  (ProfileUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (userObj == null|| userObj.equals("anonymousUser")) {
         throw new IllegalAccessException("Not Authorized");
      }
      // Get the tenant the user is logged in for (done using select-store)
      String tenant = userObj.getTenantId();
      return assignTenant(proceedingJoinPoint, tenant);
   }
*/

   private Object assignTenant(ProceedingJoinPoint proceedingJoinPoint, String tenant) throws Throwable {
      try {
         TenantContext.setCurrentTenant(tenant);
//         TenantContext.setCurrentProfile(tenant);
      } finally {
         Object retVal;
         retVal = proceedingJoinPoint.proceed();
         TenantContext.clear();
         return retVal;
      }
   }
}
