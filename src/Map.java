import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map {

    public Map(){
        System.out.println(world());
    }

    public ArrayList<String> world() {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src\\world"))) {
            String text;
            while ((text = br.readLine()).matches("\b;\b")) {
                list.add(text);
            }
        } catch (IOException e) {

        }
        return list;
    }

}
