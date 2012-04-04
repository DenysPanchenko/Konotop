package konotop;

public class Konotop {
    public static void main(String[] args) {
        //Gui app = new Gui();
        CParser parser = new CParser("int main(){\n var    =   number;\n var=number; \n}");
        System.out.print(parser.Parse());
    }
};