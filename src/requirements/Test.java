package requirements;

import java.io.File;
import java.io.IOException;

/**
 * Student information systems Brazil, MG
 *
 * @author Eduardo Almeida <edualmeida.a7@gmail.com>
 * @version 1.0
 * @since April 07, 2020
 */
public class Test {
    /**
     * class of tests, it may be that the test links have expired or are invalid
     *
     * @param args
     */
    public static void main(String[] args) {

        Requisition requisition = new Requisition();
        requisition.setFishIn("https://postman-echo.com/get?foo1=bar1&foo2=bar2");
        requisition.fish_In();
        requisition.seeResponseGet();
        requisition.setSendTo("https://reqres.in/api/users");
        File file = new File("src/test.json");
        requisition.setFile(file);
        requisition.send_To_Multipart();
        requisition.seeResponsePost();

        //
        System.out.println();
        // OR

        try {
            String linkGET = "https://postman-echo.com/get?foo1=bar1&foo2=bar2";
            String linkPOST = "https://reqres.in/api/users";
            File file1 = new File("src/test.json");
            Requisition requisition1 = new Requisition(linkGET, linkPOST, file1);
            requisition.fish_In();
            requisition.seeResponseGet();
            requisition.send_To_Multipart();
            requisition.seeResponsePost();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
