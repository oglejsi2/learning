/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Robert
 * Created: 03-Oct-2018
 */


  CREATE TABLE "ROBERT"."SERVER_LIST" 
   (	"SL_URL" VARCHAR2(500 BYTE), 
	"SL_USERNAME" VARCHAR2(500 BYTE), 
	"SL_PASSWORD" VARCHAR2(500 BYTE)
   );

    insert into server_list(sl_url,	sl_username,	sl_password)
    values ('jdbc:oracle:thin:@10.10.11.173:1521:advrcp','robert','robert')
    ;



    commit;

   create table mydata
    (   text varchar2(500), 
        ins_date date
    );

create or replace PACKAGE CONCURENTTRANSACTIONS AS 

  /* TODO enter package declarations (types, exceptions, methods etc) here */ 
	function mydata_ins(p_text in varchar2) return number ;
END CONCURENTTRANSACTIONS;

create or replace PACKAGE BODY CONCURENTTRANSACTIONS AS
	function mydata_ins(p_text in varchar2) return number as
	/*
		return -1 - error
		return 1 - ok
	*/
	locReturn number;	
	begin
		locReturn:=-1;
		insert into mydata(text, ins_date)
		values (p_text, sysdate);
		locReturn:=1;
		return locReturn;		
	end;

END CONCURENTTRANSACTIONS;

Test the function

DECLARE
  P_TEXT VARCHAR2(200);
  v_Return NUMBER;
BEGIN
  P_TEXT := 'a';

  v_Return := CONCURENTTRANSACTIONS.MYDATA_INS(
    P_TEXT => P_TEXT
  );
DBMS_OUTPUT.PUT_LINE('v_Return = ' || v_Return);
END;
/



