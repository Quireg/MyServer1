import core.DataMapperException;
import core.DbMapper;
import exampleClasses.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, SQLException, DataMapperException {
        ApplicationContext context =
                new ClassPathXmlApplicationContext(new String[] {"services.xml"});

//        User u = context.getBean("us", User.class);
//        System.out.println(u.getAge());
//        Company c = context.getBean("cm", Company.class);
       DbMapper db = (DbMapper) context.getBean("dataMapper");
       User u = (User) db.load(2, User.class);
        System.out.println(u.getName());
    }
}
