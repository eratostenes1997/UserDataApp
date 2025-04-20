package com.paprika.userdataapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.paprika.userdataapp.entities.User;
import com.paprika.userdataapp.repository.UserRepository;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etContrasena;

    private Button btnGuardar;
    private Button btnBuscar;
    private Button btnListar;
    private Button btnBorrar;
    private Button btnActualizar;
    private Button btnBuscarPorDocumento;

    private ListView listUsers;

    private int documento;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        this.begin();

        this.btnGuardar.setOnClickListener(this::createUser);
        this.btnListar.setOnClickListener(this::listUser);
        this.btnActualizar.setOnClickListener(this::updateUser);
        this.btnBorrar.setOnClickListener(this::deleteUser);
        this.btnBuscarPorDocumento.setOnClickListener(this::searchByDocument);
        this.btnBuscar.setOnClickListener(this::searchByLastName);
    }

    private void listUser(View view) {
        listUsers(view);
        clearFields();
    }

    private void createUser(View view) {
        capData();
        if (String.valueOf(documento).isEmpty() || usuario.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios para crear", Toast.LENGTH_SHORT).show();
            return;
        }
        User userObj = new User(documento, nombres, apellidos, usuario, pass);
        UserRepository userRepository = new UserRepository(this, view);
        userRepository.insertUser(userObj);
        clearFields();
    }

    private void updateUser(View view) {
        capData();
        if (String.valueOf(documento).isEmpty() || usuario.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRepository userRepository = new UserRepository(this, view);
        User existing = userRepository.getUserByDocument(documento);
        if (existing == null) {
            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();
            return;
        }

        User userObj = new User(documento, nombres, apellidos, usuario, pass);
        userRepository.updateUser(userObj);
        listUsers(view);
        clearFields();
    }

    private void deleteUser(View view) {
        capData();
        if (String.valueOf(documento).isEmpty()) {
            Toast.makeText(this, "Ingrese el documento para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }
        UserRepository userRepository = new UserRepository(this, view);
        User existing = userRepository.getUserByDocument(documento);
        if (existing == null) {
            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();
            return;
        }
        userRepository.deleteUser(documento);
        listUsers(view);
        clearFields();
    }

    private void searchByDocument(View view) {
        capData();
        if (String.valueOf(documento).isEmpty()) {
            Toast.makeText(this, "Ingrese el documento para buscar", Toast.LENGTH_SHORT).show();
            return;
        }
        UserRepository userRepository = new UserRepository(this, view);
        User userObj = userRepository.getUserByDocument(documento);
        ArrayList<User> result = new ArrayList<>();
        if (userObj != null) {
            result.add(userObj);
        } else {
            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, result);
        listUsers.setAdapter(adapter);
        clearFields();
    }

    private void searchByLastName(View view) {
        capData();
        if (apellidos.isEmpty()) {
            Toast.makeText(this, "Ingrese el apellido para buscar", Toast.LENGTH_SHORT).show();
            return;
        }
        UserRepository userRepository = new UserRepository(this, view);
        ArrayList<User> result = userRepository.getUsersByLastName(apellidos);
        if (result.isEmpty()) {
            Toast.makeText(this, "No se encontraron usuarios con ese apellido", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, result);
        listUsers.setAdapter(adapter);
        clearFields();
    }

    private void listUsers(View view){
        UserRepository userRepository = new UserRepository(this, view);
        ArrayList<User> list = userRepository.getUserList();
        ArrayAdapter<User> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        this.listUsers.setAdapter(arrayAdapter);
    }

    private void capData() {
        String doc = etDocumento.getText().toString().trim();
        this.documento = doc.isEmpty() ? 0 : Integer.parseInt(doc);
        this.usuario = etUsuario.getText().toString().trim();
        this.nombres = etNombres.getText().toString().trim();
        this.apellidos = etApellidos.getText().toString().trim();
        this.pass = etContrasena.getText().toString().trim();
    }

    private void clearFields() {
        etDocumento.setText("");
        etUsuario.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etContrasena.setText("");
    }

    private void begin() {
        this.etDocumento = findViewById(R.id.etDocumento);
        this.etUsuario = findViewById(R.id.etUsuario);
        this.etNombres = findViewById(R.id.etNombres);
        this.etApellidos = findViewById(R.id.etApellidos);
        this.etContrasena = findViewById(R.id.etContrasena);

        this.btnGuardar = findViewById(R.id.btnRegister);
        this.btnBuscar = findViewById(R.id.btnBuscar);
        this.btnListar = findViewById(R.id.btnListar);
        this.btnBorrar = findViewById(R.id.btnLimpiar);
        this.btnActualizar = findViewById(R.id.btnActualizar);
        this.btnBuscarPorDocumento = findViewById(R.id.btnBuscarPorDocumento);

        this.listUsers = findViewById(R.id.lvLista);
    }
}