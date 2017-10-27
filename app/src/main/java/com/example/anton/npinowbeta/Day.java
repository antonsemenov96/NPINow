package com.example.anton.npinowbeta;

import java.util.List;

/**
 * Created by tonny on 14.03.2017.
 */

public class Day {
    String Week;
    String Name;

    String onetime;
    String oneroom;
    String onedisctype;
    String onediscname;

    String twotime;
    String tworoom;
    String twodisctype;
    String twodiscname;

    String threetime;
    String threeroom;
    String threedisctype;
    String threediscname;

    String fourtime;
    String fourroom;
    String fourdisctype;
    String fourdiscname;

    String NoLessontime1 = "09:00-10:30";
    String NoLessontime2 = "10:45-12:15";
    String NoLessontime3 = "12:45-14:15";
    String NoLessontime4 = "14:30-16:00";
    String NoLessonroom = " ";
    String NoLessondisctype = " ";
    String NoLessondiscname = "Нет занятия\n";

    String NoDaytime = "Выходной";
    String NoDayroom = " ";
    String NoDaydisctype = " ";
    String NoDaydiscname = " ";

    Day(boolean workFlag, String WeekNum, String DayName, List<String[]> Lessons) {
        this.Name = DayName;
        this.Week = WeekNum;
        if (workFlag){
            //рабочий день
            if(Lessons.get(0) != null){
                this.onetime = Lessons.get(0)[4];
                this.oneroom = Lessons.get(0)[0];
                this.onedisctype = Lessons.get(0)[2];
                this.onediscname = Lessons.get(0)[3];

            }
            else{
                this.onetime = NoLessontime1;
                this.oneroom = NoLessonroom;
                this.onedisctype = NoLessondisctype;
                this.onediscname = NoLessondiscname;
            }
            if(Lessons.get(1) != null){
                this.twotime = Lessons.get(1)[4];
                this.tworoom = Lessons.get(1)[0];
                this.twodisctype = Lessons.get(1)[2];
                this.twodiscname = Lessons.get(1)[3];
            }
            else{
                this.twotime = NoLessontime2;
                this.tworoom = NoLessonroom;
                this.twodisctype = NoLessondisctype;
                this.twodiscname = NoLessondiscname;
            }
            if(Lessons.get(2) != null){
                this.threetime = Lessons.get(2)[4];
                this.threeroom = Lessons.get(2)[0];
                this.threedisctype = Lessons.get(2)[2];
                this.threediscname = Lessons.get(2)[3];
            }
            else {
                this.threetime= NoLessontime3;
                this.threeroom = NoLessonroom;
                this.threedisctype = NoLessondisctype;
                this.threediscname = NoLessondiscname;
            }
            if(Lessons.get(3) != null){
                this.fourtime = Lessons.get(3)[4];
                this.fourroom = Lessons.get(3)[0];
                this.fourdisctype = Lessons.get(3)[2];
                this.fourdiscname = Lessons.get(3)[3];
            }
            else {
                this.fourtime = NoLessontime4;
                this.fourroom  = NoLessonroom;
                this.fourdisctype = NoLessondisctype;
                this.fourdiscname = NoLessondiscname;
            }
        }
        else{
            this.onetime = NoDaytime;
            this.oneroom = NoDayroom;
            this.onedisctype = NoDaydisctype;
            this.onediscname = NoDaydiscname;
        }
    }
}
