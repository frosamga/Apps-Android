package com.example.agenda;

public class Contacto {

	long id;
	String Nombre; 
	String telefono;
	String direccion;
	String correo;
	
	Contacto (long idd,String Nombred,String telefonod,String direcciond, String correod){
		id=idd;
		Nombre=Nombred;
		telefono=telefonod;
		direccion=direcciond;
		correo=correod;
	}
	
	Contacto (String Nombred,String telefonod,String direcciond, String correod){
		Nombre=Nombred;
		telefono=telefonod;
		direccion=direcciond;
		correo=correod;
	}
	

	Contacto (){
		id=0;
		Nombre=null;
		telefono=null;
		direccion=null;
		correo=null;
	}
	public void setNombre(String Nombred){
		Nombre=Nombred;
	}
	
	public void setExpediente (long idd){
		id=idd;
	}
	
	public void setTelefono (String telefonod){
		telefono=telefonod;
	}
	public void setDireccion (String dir){
		direccion=dir;
	}
	public void setCorreo (String correod){
		correo=correod;
	}
	public String getNombre(){
		return Nombre;
	}
	
	public long getid (){
		return id;
	}
	
	public String  getTelefono (){
		return telefono;
	}
	public String getDireccion(){
		return  direccion;
	}
	public String getCorreo(){
		return correo;
	}

}
