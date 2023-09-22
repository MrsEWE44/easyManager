package android.content;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;


public class IntentSender implements Parcelable {

    public IntentSender(IIntentSender target) {

    }



    public IntentSender(IIntentSender target, IBinder whitelistToken) {

    }

    public IntentSender(IBinder target) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel parcel, int i) {

    }
}
