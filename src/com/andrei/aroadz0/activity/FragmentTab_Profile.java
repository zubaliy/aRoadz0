package com.andrei.aroadz0.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.andrei.aroadz0.R;
import com.andrei.aroadz0.controller.Uploader;
import com.andrei.aroadz0.model.User;
import com.andrei.aroadz0.utils.Config;
import com.andrei.aroadz0.utils.Toasts;



	
	 
	public class FragmentTab_Profile extends SherlockFragment implements OnClickListener, OnCheckedChangeListener {

		private final String LOG_TAG = "zTabProfile";
		private User user = null;
		
		
		EditText txt_name, txt_email, txt_password;
		private Button btnSave, btnUnregister;
		private TextView txt_user;
		private CheckBox cbox_password;
		
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Get the view from fragmenttab1.xml
	        View view = inflater.inflate(R.layout.fragment_profile, container, false);
	        
	        txt_user = (TextView) view.findViewById(R.id.txt_user);
	        
	        btnSave = (Button) view.findViewById(R.id.btn_save);
			btnSave.setOnClickListener(this);
			
			btnUnregister = (Button) view.findViewById(R.id.btn_unregister);
			btnUnregister.setOnClickListener(this);
			
			cbox_password = (CheckBox) view.findViewById(R.id.cbox_password);
			cbox_password.setOnCheckedChangeListener(this);
			
			
	        txt_name = (EditText) view.findViewById(R.id.txtb_name);
	        txt_email = (EditText) view.findViewById(R.id.txtb_email);
	        txt_password = (EditText) view.findViewById(R.id.txtb_password);
	        
	        user = User.getInstance();
	        
        	txt_email.setText(user.getEmail());
	        txt_name.setText(user.getName());
	        txt_password.setText(user.getPassword());
	        

	        if (User.getInstance().isRegistered()) {
				 txt_email.setEnabled(false);
				 txt_name.setEnabled(false);
				 txt_password.setEnabled(false);
				 btnSave.setEnabled(false);
				 cbox_password.setEnabled(false);
	        }
	        
	        
	        txt_user.setText(user.toString());
	        
	        Log.d(LOG_TAG, "onCreateView()");
	        return view;
	    }
	 
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
	    
		@Override
		public void onClick(View v) {

			switch(v.getId()){
				case R.id.btn_save:
					btnSave();
					break;
				case R.id.btn_unregister:
					btnUnregister();
					break;
				default:
					Toasts.showError("Unknown Click");
					break;
			}
		}
		
		private void btnUnregister() {
			Editor e = Config.getContext().getSharedPreferences(Config.APP_NAME ,Context.MODE_PRIVATE).edit();
			e.putString(User._REGISTERED, "false");
			e.commit();
		}

		@Override
		public void onCheckedChanged(CompoundButton v, boolean isChecked) {
			switch(v.getId()){
			case R.id.cbox_password:
				cboxClick(isChecked);
				break;
			default:
				Toasts.showError("Unknown Click");
				break;
		}
			
		}

		private void cboxClick(boolean isChecked) {
			if(isChecked) {
                txt_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
            	txt_password.setInputType(129);
            }
			
		}

		private void btnSave() {
			
			user.setName(txt_name.getText().toString());
			user.setEmail(txt_email.getText().toString());
			user.setPassword(txt_password.getText().toString());
			user.commit();
			Toasts.showGreenMessage("Saved");
				Uploader.getInstance().register();	
			
			//TODO eerst wachten op de response van Uploader, dan if uitvoeren
	        if (User.getInstance().isRegistered()) {
	        	txt_email.setEnabled(false);    
		        txt_name.setEnabled(false);    
		        txt_password.setEnabled(false);
	        	btnSave.setEnabled(false); 
	        	cbox_password.setEnabled(false);
	        }
			
		}


	 
	}
