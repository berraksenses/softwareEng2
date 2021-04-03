
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author studente
 */

    
   
 public class Client {
   static InitialContext ictx = null;

   public static void main (String[] args) throws JMSException{
       
    Properties props = new Properties();
    props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
    props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
     InitialContext ictx;
       try {
           ictx = new InitialContext(props);

       TopicConnectionFactory tcf = ( TopicConnectionFactory) ictx.lookup("ConnectionFactory");
  
       
      
      TopicConnection tconn = tcf.createTopicConnection();
      TopicSession tsession = tconn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
       Topic topic = tsession.createTopic("dynamicTopics/professors");


      TopicSubscriber tsub = tsession.createSubscriber(topic);
       tsub.setMessageListener(new MsgListener());
      
      tconn.start();
      System.in.read();
       } catch (NamingException ex) {
           Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
}

class MsgListener implements MessageListener{
   String id;
   public MsgListener(){id="";}
   @Override
   public void onMessage(Message msg){
      TextMessage tMsg = (TextMessage) msg;
      try{
          String id = tMsg.getStringProperty("id");
          
          try { // Call Web Service Operation
              com.mycompany.exampleexam.ExamImpService service = new com.mycompany.exampleexam.ExamImpService();
              com.mycompany.exampleexam.ExamInt port = service.getExamImpPort();
              // TODO initialize WS operation arguments here
              java.lang.String arg0 = "";
              // TODO process result here
              com.mycompany.exampleexam.Professor result = port.getDetails(id);
              System.out.println("Result = "+result.getName());
          } catch (Exception ex) {
              // TODO handle custom exceptions here
          }

         System.out.println("id: "+id);
      }catch(JMSException e){
         e.printStackTrace();
      }
   }
}
    
    


