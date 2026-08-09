package android.os;

import java.util.ArrayList;
import java.util.List;

public class Parcel {
    public static Parcel obtain(){
        return null;
    }

    public final byte[] marshall(){
        return null;
    }

    public final void recycle() {}


    public final void unmarshall(byte[] data, int offset, int length){}

    public final void setDataPosition(int pos){}

    public final <T extends Parcelable> void writeTypedList(List<T> val){}

    public final <T> ArrayList<T> createTypedArrayList(Parcelable.Creator<T> c) {
        return null;
    }

}