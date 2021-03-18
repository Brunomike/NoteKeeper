package com.example.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;
//Make noteInfo class as an extra to be passed from one activity to another--make it Parcelable
//Inorder to make NoteInfo class parcelable everything it references needs to be parcelable i.e mCourses,mTitle,mText
public final class NoteInfo implements Parcelable{
    private CourseInfo mCourse;
    private String mTitle;//can be parceled directly
    private String mText;//can be parceled directly

    public NoteInfo(CourseInfo course, String title, String text) {
        mCourse = course;
        mTitle = title;
        mText = text;
    }

    private NoteInfo(Parcel parcel) {
//Cannot be called outside our noteInfo class
        mCourse=parcel.readParcelable(CourseInfo.class.getClassLoader());
        //We need to pass a class loader when reading a parcelable--Provides information on how to create instances of a type
        mTitle=parcel.readString();
        mText=parcel.readString();
    }

    public CourseInfo getCourse() {
        return mCourse;
    }

    public void setCourse(CourseInfo course) {
        mCourse = course;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    private String getCompareKey() {
        return mCourse.getCourseId() + "|" + mTitle + "|" + mText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteInfo that = (NoteInfo) o;

        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    @Override
    public String toString() {
        return getCompareKey();
    }


    //Implement parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }
    //indicates/used when we have special behaviors/needs and generally can return 0

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(mCourse,0);
        parcel.writeString(mTitle);
        parcel.writeString(mText);
    }
//Receives a Parcel instance and use Parcel.writeXX to store content
    //Is a a parcelable.Creator implementation
    /**-createFromParcel
     * Responsible to create new type instance
     * Receives a Parcel instance
     * Uses Parcel.readXX to access content and set it to out type
     *
     * -newArray
     * Receives a size
     * Responsible to create array of our type of the specified type
     *
     */
    //Code to make our class re-creatable from a parcel
    public  static  final Parcelable.Creator<NoteInfo> CREATOR =
            new Parcelable.Creator<NoteInfo>(){
                @Override
                public NoteInfo createFromParcel(Parcel parcel) {
                   //Parcel values must be accessed in the same order they were written

                    return new NoteInfo(parcel);
                }

                @Override
                public NoteInfo[] newArray(int size) {
                    return new NoteInfo[size];
                }
            };
    //Information returned will be used to create instances of our NoteInfo class
}
