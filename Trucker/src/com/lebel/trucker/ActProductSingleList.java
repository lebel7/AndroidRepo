package com.lebel.trucker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import com.ianywhere.ultralitejni12.ConfigPersistent;
import com.ianywhere.ultralitejni12.DatabaseManager;
import com.ianywhere.ultralitejni12.ResultSet;
import com.ianywhere.ultralitejni12.StreamHTTPParms;
import com.ianywhere.ultralitejni12.SyncParms;
import com.ianywhere.ultralitejni12.ULjException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ActProductSingleList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyt_productlist);
		try {
            setupUiEvents();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ULjException e) {
            e.printStackTrace();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_product_single_list, menu);
		return true;
	}
	
	private void setupUiEvents() throws IOException, ULjException {
		doTaskAsync doTask = new doTaskAsync();
        doTask.execute();
    }
	

    //Clean Directory, Copy DB, Sync remote DB and select to populate our list
    private class doTaskAsync extends AsyncTask<Void, Integer, ArrayList <String>> {
        private ArrayList<String> ProductList;
        private ConfigPersistent _config;
        private com.ianywhere.ultralitejni12.Connection _dbconn;
        private static final int defaultPort = 9393;
        //private static final String host = "192.168.10.248";
        //private static final String host = "testserver";
        private static final String host = "10.0.2.2";
        private static final String defaultUsername = "dba";
        private static final String defaultPassword = "sql";
        private static final String db = "demo_remote.udb";
        protected ProgressDialog dialog;
        int progressIncrement = 0;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ActProductSingleList.this);
            CharSequence message = "Working hard...very hard...";
            CharSequence title = "Please Wait";
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage(message);
            dialog.setTitle(title);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //dialog.setProgress(0);
            //dialog.setMax(100);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList <String> result) {
            ListView lv = (ListView) findViewById(R.id.lvProductList);
            if (result != null) {
            	lv.setAdapter(new ArrayAdapter<String>(ActProductSingleList.this, android.R.layout.simple_list_item_1, result));
            }
            dialog.dismiss();
            Toast.makeText(ActProductSingleList.this, "Android successfully copied the Database to the device", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.incrementProgressBy(progressIncrement);
        }

        @Override
        protected ArrayList <String> doInBackground(Void... unused) {

            try {
                String dbFullName = ActProductSingleList.this.getApplicationInfo().dataDir + "/" + db;

                File dbFile = new File(dbFullName);
                FilenameFilter textFilter = new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        String lowercaseName = name.toLowerCase(Locale.getDefault());
                        if (lowercaseName.contains("_remote")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                };
                File[] files = dbFile.listFiles(textFilter);
                if (files != null) {
                	for (File file : files) {
                        if (file.exists()) {
                            file.delete();
                        }
                	}
                
                }
                
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   Copy Database to Directory  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                InputStream myInput = null;

                File file = new File(dbFullName);
                myInput = ActProductSingleList.this.getApplicationContext().getAssets().open(db);
                String outFileName = dbFullName;
                OutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer))>0) {
                    myOutput.write(buffer, 0, length);
                    progressIncrement = ((int) (length / (float) file.length()) * 100);
                    publishProgress(progressIncrement);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();


                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   Copy Database to Directory  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

                //Connect
                _config = DatabaseManager.createConfigurationFileAndroid(db, ActProductSingleList.this.getApplicationContext());
                _config.setUserName(defaultUsername);
                _config.setPassword(defaultPassword);
                _config.setPageSize(8192);

                _dbconn = DatabaseManager.connect(_config);

                //Sync
                SyncParms sp = _dbconn.createSyncParms("dba", "demo_scriptversion");
                sp.setPassword("sql");
                sp.setPublications("demo_publication");
                StreamHTTPParms httpParms = sp.getStreamParms();
                httpParms.setHost(host);
                httpParms.setPort(defaultPort);
                _dbconn.synchronize(sp);
                

                //Query
                //String qry = "SELECT TOP 20 * FROM DBA.Product WHERE DBA.Product.Artist IS NOT NULL ORDER BY DBA.Product.Artist ASC";
                String qry = "SELECT TOP 500 * FROM DBA.Product p WHERE p.Artist <> '' ORDER BY p.Artist ASC";
                com.ianywhere.ultralitejni12.PreparedStatement ps = _dbconn.prepareStatement(qry);
                ResultSet rs = ps.executeQuery();
                ProductList = new ArrayList<String>();
                if (!rs.next()) {
                    System.out.println("No records found");
                } else {
                    do {
                        //ProductList.add(rs.getString(1));
                    	ProductList.add(rs.getString("Artist"));
                    } while (rs.next());
                    rs.close();
                    ps.close();
                }
                
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ULjException e) {
                e.printStackTrace();
            }
            return ProductList;
        }
    }


	@Override
	protected void onDestroy() {
		Log.i("My AsyncTask [doTaskAsync]", System.currentTimeMillis() / 1000L + "  onDestroy()");
		//doTaskAsync.cancel(true);
		super.onDestroy();
	}

}
