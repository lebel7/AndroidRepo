package com.lebel.trucker;

import java.util.ArrayList;

import com.ianywhere.ultralitejni12.Connection;
import com.ianywhere.ultralitejni12.PreparedStatement;
import com.ianywhere.ultralitejni12.ResultSet;
import com.ianywhere.ultralitejni12.ULjException;

import android.os.Bundle;
import android.app.Activity;
import android.database.SQLException;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActViewStaff extends Activity {
	private ArrayList<String> StaffList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyt_view_staff);
		
		try {
			setupUiEvents();
		} catch (ULjException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setupUiEvents() throws ULjException {
        try {
            Connection _dbconn = (Connection) UltraDatabaseHelper.getInstance(this.getApplicationContext());
            String qry = "SELECT FirstName FROM tblAvailableStaff";
            PreparedStatement ps = _dbconn.prepareStatement(qry);
            ResultSet rs = ps.executeQuery();
            StaffList = new ArrayList<String>();
            while (rs.next()) {
                StaffList.add(rs.getString(0));
            }
            rs.close();
            ps.close();
            ListView lv = (ListView) findViewById(R.id.lvStaffList);
            lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, StaffList));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_view_staff, menu);
		return true;
	}

}
