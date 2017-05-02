
import java.util.Properties;

public class TestMail {

    private static final Properties props;
    private final static String USER_NAME = "paymygift@gmail.com";
    private final static String PASSWORD = "Copchik17";

    static {
        props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
    }

   /* @Test
    public void test() {
        String subject = "Ваш пароль в приложении core.paymygift";
        try {
            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(USER_NAME, PASSWORD);
                        }
                    });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("paymygift@gmail.com"));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("dmitrenko1213@bk.ru", false));
            msg.setSubject(subject);
            msg.setText("asdasdasd");
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (MessagingException ignored) {
        }
    }*/



    /*
       @Test
        public void getPartnerSessionId(){
            EasyPayAPI easyPayAPI =new EasyPayAPI();
            String s = easyPayAPI.securityLogonPartner();
            System.out.println(s);
        }
     /*

        @Test
        public void deleteUser(){
            EasyPayAPI easyPayAPI = new EasyPayAPI();
            String sessionId = easyPayAPI.securityLogonPartner();
            easyPayAPI.deleteUser(sessionId,"380635927798");
        }

        @Test
        public void registrationUser(){
            EasyPayAPI easyPayAPI = new EasyPayAPI();
            easyPayAPI.registration("servletContext","servletContext");
        }

        @Test
        public void LoginActivation(){
            EasyPayAPI easyPayAPI = new EasyPayAPI();
            easyPayAPI.loginActivation("790b8a03-8f2f-4433-b8e0-1e692bd795c6","380635927798");
        }

        @Test
        public void activation(){
            EasyPayAPI easyPayAPI = new EasyPayAPI();
            easyPayAPI.activation("0c656c9d-fc0d-41e2-8b04-09896a1a6d47","8136");
        }

        @Test
        public void addWallet(){
            EasyPayAPI easyPayAPI = new EasyPayAPI();
            easyPayAPI.addWalletAccount("bfa8994e64f344d99b84bdce0b88dbee","380635927798");
        }*/

}
