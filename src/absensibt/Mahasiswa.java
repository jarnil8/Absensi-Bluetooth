/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absensibt;

/**
 *
 * @author bey0nd
 */
public class Mahasiswa {
    private String nama;
    private String NRP;
    private String btAddress;
    private boolean hadir;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNRP() {
        return NRP;
    }

    public void setNRP(String NRP) {
        this.NRP = NRP;
    }

    public String getBtAddress() {
        return btAddress;
    }

    public void setBtAddress(String btAddress) {
        this.btAddress = btAddress;
    }

    public boolean isHadir() {
        return hadir;
    }

    public void setHadir(boolean hadir) {
        this.hadir = hadir;
    }
    
    public Mahasiswa(String nama, String NRP, String btAddress) {
        this.nama = nama;
        this.NRP = NRP;
        this.btAddress = btAddress;
        
        this.hadir = false;
    }
}
