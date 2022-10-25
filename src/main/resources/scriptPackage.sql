create or replace PACKAGE employees_jobs IS
 PROCEDURE newEmployee (genderIdParam IN number, jobIdParam IN number, nameParam IN VARCHAR2, lastNameParam IN VARCHAR2, birthdateParam IN VARCHAR2, pcidMSG OUT NUMBER ,pcMsg OUT NUMBER);

 PROCEDURE totlHours (employeeIdParam IN number, starDate IN VARCHAR2, endDate IN VARCHAR2, tipoOper IN number, totalHoursMSG OUT NUMBER, pcMsg OUT NUMBER);
END;

/

create or replace PACKAGE BODY employees_jobs IS

  PROCEDURE newEmployee (genderIdParam IN number, jobIdParam IN number, nameParam IN VARCHAR2, lastNameParam IN VARCHAR2, birthdateParam IN VARCHAR2, pcidMSG OUT NUMBER ,pcMsg OUT NUMBER)
  IS
  
  flagNames NUMBER DEFAULT 0;
  flagYearOld NUMBER DEFAULT 0;
  flagFoundJob NUMBER DEFAULT 0;
  resId NUMBER DEFAULT 0;
  resFlag NUMBER DEFAULT 0;
  viCuenta NUMBER DEFAULT 0;

  BEGIN
  
  -- sabessi existe el puesto
    SELECT COUNT(1)
    INTO flagFoundJob
    FROM JOBS a
    WHERE A.JOBSID = jobIdParam;
    
     IF (flagFoundJob = 0) THEN
      
      pcidMSG := 0;
      pcMsg := 0;

      RETURN;
    END IF;
   
   
   -- validar que no exista nombre y apellido 
   viCuenta:= 0; 
   
    SELECT COUNT(1)
    INTO flagNames
    FROM EMPLOYEES a
    WHERE A.name = nameParam
    and a.lastname = lastNameParam;
    
    IF (flagNames > 0) THEN
      
      pcidMSG := 0;
      pcMsg := 0;
      
      RETURN;
    END IF;
    
    
     -- validar edad
   
      select
      round((TRUNC(SYSDATE) - to_date(birthdateParam,'DD/MM/YYYY')) / 365) INTO flagYearOld
      from dual;
    
    IF (flagYearOld < 18) THEN
      
      pcidMSG := 0;
      pcMsg := 0;
      
      RETURN;
    END IF;
    
    SELECT MAX(EMPLOYEEID) + 1
    INTO resId
    FROM EMPLOYEES a;
    
    insert into EMPLOYEES values 
    (resId,genderIdParam,jobIdParam,nameParam,lastNameParam,TO_DATE(birthdateParam,'DD/MM/YYYY'));

    
   pcidMSG := resId;
   pcMsg := 1;
      

  END;
  
  
  ---- total hours - salary
  PROCEDURE totlHours (employeeIdParam IN number, starDate IN VARCHAR2, endDate IN VARCHAR2, tipoOper IN number, totalHoursMSG OUT NUMBER, pcMsg OUT NUMBER)
  IS
  
  flagEmployee NUMBER DEFAULT 0;
  flagDates NUMBER DEFAULT 0;
  viCuenta NUMBER DEFAULT 0;

  BEGIN
  
    SELECT COUNT(1)
    INTO flagEmployee
    FROM EMPLOYEES a
    WHERE A.EMPLOYEEID = employeeIdParam;
    
    IF (flagEmployee = 0) THEN
      
      totalHoursMSG := 0;
      pcMsg := 0;
      
      RETURN;
    END IF;
    
        
    IF (tipoOper = 0) THEN
    
    SELECT  decode(sum(emho.WORKED_HOURS), null, 0,sum(emho.WORKED_HOURS)) 
    into viCuenta
    FROM EMPLOYEES em,EMPLOYEE_WORKED_HOURS emho
    where  em.EMPLOYEEID = employeeIdParam
    and em.EMPLOYEEID = emho.EMPLOYEE_ID
    and emho.WORKED_DATE BETWEEN to_date(starDate,'dd/mm/yyyy') and to_date(endDate,'dd/mm/yyyy');
    
    else
    
    SELECT  decode(sum(j.salary), null, 0,sum(j.salary))
    into viCuenta
    FROM EMPLOYEES em,EMPLOYEE_WORKED_HOURS emho, jobs j
    where  em.EMPLOYEEID = employeeIdParam
    and em.EMPLOYEEID = emho.EMPLOYEE_ID
    and em.JOB_ID = j.JOBSID
    and emho.WORKED_DATE BETWEEN to_date(starDate,'dd/mm/yyyy') and to_date(endDate,'dd/mm/yyyy');
    
    END IF;
    
    
   totalHoursMSG := viCuenta;
   pcMsg := 1;
      

  END;
END;