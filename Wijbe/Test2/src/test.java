import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class test {

    public static void main(String[] args){
        try {
            System.out.println("Start");
            Process pythonprocess = Runtime.getRuntime().exec("python convert.py");
            InputStream i = pythonprocess.getInputStream();
            String s = printStream(i);

            System.out.println("Started");
            System.out.println(s);
            InputStream er = pythonprocess.getErrorStream();
            System.out.println(printStream(er));
            while (pythonprocess.isAlive()){
                try {
                    Thread.sleep(10);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
        }

    }
    public static String printStream(InputStream inputStream){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader((new InputStreamReader(inputStream)));
            while((line = br.readLine()) != null){
                sb.append(line);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (br != null){
                try{
                    br.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
