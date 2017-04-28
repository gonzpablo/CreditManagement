package cred;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientView extends JInternalFrame {
	
	private static final long serialVersionUID = 1345838347492871870L;
	private JPanel panel;	
	static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

	private JTextField nombre = new JTextField(10);
    private JTextField apellido = new JTextField(10);
    private JTextField direccion = new JTextField(10);
    private JTextField telefono = new JTextField(10);
    
	private JButton guardarButton = new JButton("Guardar");    
	private JButton cancelarButton = new JButton("Cancelar");
	
	private JLabel nombreLbl = new JLabel("Nombre");
	private JLabel apellidoLbl = new JLabel("Apellido");
	private JLabel direccionLbl = new JLabel("Dirección");
	private JLabel telefonoLbl = new JLabel("Teléfono");	
	
	
	public ClientView() {
        super("Alta de Cliente", 
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable

          //...Create the GUI and put it in the window...

		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(2,2,2,2);		
		
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());

//		Nombre		
		c.gridx = 0;
		c.gridy = 0;		
		
		panel.add(nombreLbl, c);

//		Nombre Input
		c.gridx = 1;
		c.gridy = 0;		

		panel.add(nombre, c);

//		Apellido	
		c.gridx = 0;
		c.gridy = 1;		
		
		panel.add(apellidoLbl, c);

//		Apellido Input
		c.gridx = 1;
		c.gridy = 1;		

		panel.add(apellido, c);		
		
		
//		Direccion	
		c.gridx = 0;
		c.gridy = 2;		
		
		panel.add(direccionLbl, c);

//		Direccion Input
		c.gridx = 1;
		c.gridy = 2;		
		
		panel.add(direccion, c);
			
		
		
//		Telefono
		c.gridx = 0;
		c.gridy = 3;		
		
		panel.add(telefonoLbl, c);

//		Telefono Input
		c.gridx = 1;
		c.gridy = 3;		
		
		panel.add(telefono, c);		
			
		
//		Botón Guardar
		c.gridx = 0;
		c.gridy = 4;		
				
		panel.add(guardarButton, c);		

//		Botón Cancelar
		c.gridx = 1;
		c.gridy = 4;		
				
		panel.add(cancelarButton, c);		
		
		
		add(panel);
          
        //...Then set the window size or call pack...
        setSize(300,300);

        //Set the window's location.
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);				
	}
	
    public String getNombre() {
		return nombre.toString();
	}

	public void setNombre(String nombre) {
		this.nombre.setText(nombre);
	}

	public String getApellido() {
		return apellido.toString();
	}

	public void setApellido(String apellido) {
		this.apellido.setText(apellido);
	}

	public String getDireccion() {
		return direccion.toString();
	}

	public void setDireccion(String direccion) {
		this.direccion.setText(direccion);
	}

	public String getTelefono() {
		return telefono.toString();
	}

	public void setTelefono(String telefono) {
		this.telefono.setText(telefono);
	}    
}
