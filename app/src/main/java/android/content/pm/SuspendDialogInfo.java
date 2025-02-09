package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

public class SuspendDialogInfo implements Parcelable {
    protected SuspendDialogInfo(Parcel in) {
    }

    public static final Creator<SuspendDialogInfo> CREATOR = new Creator<SuspendDialogInfo>() {
        @Override
        public SuspendDialogInfo createFromParcel(Parcel in) {
            return new SuspendDialogInfo(in);
        }

        @Override
        public SuspendDialogInfo[] newArray(int size) {
            return new SuspendDialogInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
