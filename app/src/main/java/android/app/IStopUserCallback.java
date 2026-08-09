package android.app;

public interface IStopUserCallback{
    void userStopped(int userId);
    void userStopAborted(int userId);
}
