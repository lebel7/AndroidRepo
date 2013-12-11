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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ULjException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_product_single_list, menu);
		return true;
	}
	
	private void setupUiEvents() throws IOException, ULjException {
        new doTaskAsync().execute(this.getApplicationInfo().dataDir);
    }

    //Clean Directory, Copy DB, Sync remote DB and select to populate our list
    private class doTaskAsync extends AsyncTask<String, Integer, ArrayList <String>> {
        private ArrayList<String> ProductList;
        private ConfigPersistent _config;
        private com.ianywhere.ultralitejni12.Connection _dbconn;
        private static final int defaultPort = 9191;
        private static final String host = "192.168.10.248";
        //private static final String host = "testserver";
        //private static final String host = "10.0.2.2";
        private static final String defaultUsername = "dba";
        private static final String defaultPassword = "sql";
        //private static final String db = "proper_remote.udb";
        private static final String db = "brink_remote.udb";
        protected ProgressDialog dialog;
        int progressIncrement = 0;
        @Override
        protected void onPreExecute() {
            //copyDialog = ProgressDialog.show(ActProduct.this.getApplicationContext(), "Cleaning up Database Directory", "Please Wait");
            dialog = new ProgressDialog(ActProductSingleList.this);
            CharSequence message = "Working hard...very hard...";
            CharSequence title = "Please Wait";
            dialog.setCancelable(true);
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
            //lv.setAdapter(new ArrayAdapter<String>(ActProductSingleList.this, android.R.layout.simple_list_item_1, result));
            //Stop progress bar
            dialog.dismiss();
            Toast.makeText(ActProductSingleList.this, "Android successfully copied the Database to the device", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);    //To change body of overridden methods use File | Settings | File Templates.
            dialog.incrementProgressBy(progressIncrement);
        }

        @Override
        protected ArrayList <String> doInBackground(String... dbTargetPath) {

            try {
                String dbFullName = ActProductSingleList.this.getApplicationInfo().dataDir + "/" + db;
                //String dbShadowFullName = dbTargetPath + "/" + dbShadowFile;
                //File fileDir = new File(String.valueOf(dbTargetPath));   // current db directory

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
                //int deleteCount = 0;
                File[] files = dbFile.listFiles(textFilter);
                if (files != null) {
                	for (File file : files) {
                        if (file.exists()) {
                            file.delete();
                            //deleteCount += 1;
                            //System.out.print("Database file deleted !");
                        }
                	}
                
                }
                //return deleteCount >= 1;  //To change body of implemented methods use File | Settings | File Templates.
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   Copy Database to Directory  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                //String dbFullName = dbTargetPath + "/" + db;
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

                //ArrayList<String> prodList = new ArrayList<String>();

                //Connect
                _config = DatabaseManager.createConfigurationFileAndroid(db, ActProductSingleList.this.getApplicationContext());
                _config.setUserName(defaultUsername);
                _config.setPassword(defaultPassword);
                _config.setPageSize(8192);

                _dbconn = DatabaseManager.connect(_config);

                //Sync
                SyncParms sp = _dbconn.createSyncParms("dba", "brink_scriptversion");
                sp.setPassword("sql"); // delete me
                sp.setPublications("brink_publication"); // delete me
                StreamHTTPParms httpParms = sp.getStreamParms();
                httpParms.setHost(host);
                httpParms.setPort(defaultPort);
                _dbconn.synchronize(sp);
                //_dbconn.commit(); delete me if causes any errors
                

                //Query
                String qry = "SELECT TOP 20 * FROM DBA.Product WHERE DBA.Product.Artist IS NOT NULL ORDER BY DBA.Product.Artist ASC";
                com.ianywhere.ultralitejni12.PreparedStatement ps = _dbconn.prepareStatement(qry);
                ResultSet rs = ps.executeQuery();
                ProductList = new ArrayList<String>();
                if (!rs.next()) {
                    System.out.println("No records found");
                } else {
                    do {
                        ProductList.add(rs.getString(1));
                    } while (rs.next());
                    rs.close();
                    ps.close();
                }
                
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ULjException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return ProductList;
        }
    }


	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("My AsyncTask [doTaskAsync]", System.currentTimeMillis() / 1000L + "  onDestroy()");
	}

}
