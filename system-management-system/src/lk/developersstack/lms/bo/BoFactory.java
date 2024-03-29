package lk.developersstack.lms.bo;

import lk.developersstack.lms.bo.custom.impl.LaptopBoImpl;
import lk.developersstack.lms.bo.custom.impl.ProgramBoImpl;
import lk.developersstack.lms.bo.custom.impl.StudentBoImpl;
import lk.developersstack.lms.dao.custom.impl.LaptopDaoImpl;


public class BoFactory {

    private static BoFactory boFactory;

    private BoFactory(){

    }

    public enum BoType{

        BOOK, STUDENT,PROGRAM,LAPTOP
    }

    public static BoFactory getInstance(){
        return boFactory==null?boFactory=new BoFactory():boFactory;
    }

    public <T> T getBo(BoType type){
        switch (type){
            case STUDENT:
                return (T) new StudentBoImpl();
            case BOOK:
                return null;
            case LAPTOP:
                return (T) new LaptopBoImpl();
            case PROGRAM:
                return (T) new ProgramBoImpl();
            default:
                return null;
        }
    }



}
