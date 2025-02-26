import javax.xml.stream.Location;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Map {

    public Map(){
        System.out.println(world());
    }

    private HashMap<Integer, Location> map = new HashMap<>();
    private int start = 0;
    private int currentPosition = start;

    public boolean LoadMap(){
        try (BufferedReader br = new BufferedReader(new FileReader("src\\world"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(";");
                Location location = new Location(
                    lines[1],
                    Integer.parseInt(lines[0]),
                    Arrays.copyOfRange(lines, 2, 5)
                );
                world.put(Integer.valueOf(lines[0]), location);
            }
            return true;
        } catch (IOException e){
            return false;
        }
    }

//
//    public ArrayList<String> world1() {
//        ArrayList<String> list = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader("src\\world"))) {
//            String text;
//            while ((text = br.readLine()) == null) {
//                list.add(text);
//            }
////            return list;
//        } catch (IOException e) {
//
//        }
//        return list;
//    }

    public String world() {
        String list = "";
        try (BufferedReader br = new BufferedReader(new FileReader("src\\world"))) {
            String text;
            while ((text = br.readLine()) != null) {
                list += text + "\n";
            }
            return list;
        } catch (IOException e) {

        }
        return "Nelze provest";
    }

}
