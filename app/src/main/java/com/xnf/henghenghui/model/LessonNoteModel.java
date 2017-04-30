package com.xnf.henghenghui.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/7.
 */
public class LessonNoteModel implements Serializable {
    private String nId;
    private String cId;
    private String noteImg;
    private String noteName;
    private String noteContent;
    private String noteDate;
    private String noteAuthor;
    private String noteUploadImg1;
    private String noteUploadImg2;
    private String noteUploadImg3;
    private String noteUploadImg4;
    private String giveLike;


    public String getnId() {
        return nId;
    }

    public void setnId(String nId) {
        this.nId = nId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteAuthor() {
        return noteAuthor;
    }

    public void setNoteAuthor(String noteAuthor) {
        this.noteAuthor = noteAuthor;
    }

    public String getNoteImg() {
        return noteImg;
    }

    public void setNoteImg(String noteImg) {
        this.noteImg = noteImg;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteUploadImg1() {
        return noteUploadImg1;
    }

    public void setNoteUploadImg1(String noteUploadImg1) {
        this.noteUploadImg1 = noteUploadImg1;
    }

    public String getNoteUploadImg2() {
        return noteUploadImg2;
    }

    public void setNoteUploadImg2(String noteUploadImg2) {
        this.noteUploadImg2 = noteUploadImg2;
    }

    public String getNoteUploadImg3() {
        return noteUploadImg3;
    }

    public void setNoteUploadImg3(String noteUploadImg3) {
        this.noteUploadImg3 = noteUploadImg3;
    }

    public String getNoteUploadImg4() {
        return noteUploadImg4;
    }

    public void setNoteUploadImg4(String noteUploadImg4) {
        this.noteUploadImg4 = noteUploadImg4;
    }

    public String getGiveLike() {
        return giveLike;
    }

    public void setGiveLike(String giveLike) {
        this.giveLike = giveLike;
    }
}
