package control;

public class ActionResult {
    private final boolean success;
    private final String message;

    public ActionResult(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    //getters
    public boolean isSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }
}

