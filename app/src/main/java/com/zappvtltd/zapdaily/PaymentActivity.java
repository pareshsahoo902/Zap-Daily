package com.zappvtltd.zapdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.Models.WalletModel;
import com.zappvtltd.zapdaily.ViewHolder.WalletViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class PaymentActivity extends AppCompatActivity {

    private String price,currentBalance;
    private AlertDialog dialog;
    private CardView pay;
    private EditText addMoney;
    private TextView balance;

    private DatabaseReference walletRef,userRef;
    private FirebaseRecyclerOptions<WalletModel> walletModelFirebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<WalletModel, WalletViewHolder> walletViewHolderFirebaseRecyclerAdapter;

    private LinearLayout add100,add200,add500,add1000;
    private RecyclerView recyclerwallet;
    private String CurrentDate;
    private String CurrentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        price=getIntent().getStringExtra("totalamount");

        if (ContextCompat.checkSelfPermission(PaymentActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PaymentActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        pay=(CardView) findViewById(R.id.Card_Vacation);
        addMoney=findViewById(R.id.Edit_Wallet);
        balance=findViewById(R.id.balance);
        add100=findViewById(R.id.add_100);
        add200=findViewById(R.id.add_200);
        add500=findViewById(R.id.add_500);
        add1000=findViewById(R.id.add_1000);
        if (price!=null){
            addMoney.setText(price);
        }
        dialog = ProgressDialogLoading.getLoadingDialog(PaymentActivity.this, "Loading...");
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    currentBalance=snapshot.child("wallet_balance").getValue().toString();
                    balance.setText(currentBalance+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerwallet=findViewById(R.id.wallet_data_recycler);
        recyclerwallet.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        loadPaymentActivity();

        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
        CurrentDate = currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currenttime.format(calendar.getTime());
        PushDownAnim.setPushDownAnimTo(add100,add200,add500,add1000,pay)
                .setScale(MODE_SCALE,
                        PushDownAnim.DEFAULT_PUSH_SCALE)

                .setDurationPush(PushDownAnim.DEFAULT_PUSH_DURATION)
                .setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION)
                .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        switch (v.getId()) {

                            case R.id.add_100:
                                addMoney.setText("100");
                                break;
                             case R.id.add_200:
                                addMoney.setText("200");
                                break;
                             case R.id.add_500:
                                addMoney.setText("500");
                                break;
                             case R.id.add_1000:
                                addMoney.setText("1000");
                                break;
                             case R.id.Card_Vacation:
                                 addMoneyToWallet();
                                 break;

                        }
                    }
                });
    }

    private void loadPaymentActivity() {

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        walletModelFirebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<WalletModel>().setQuery(databaseReference,WalletModel.class).build();
        walletViewHolderFirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<WalletModel, WalletViewHolder>(walletModelFirebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull WalletViewHolder walletViewHolder, int i, @NonNull final WalletModel walletModel) {
                walletViewHolder.details.setText("WALLET TOPUP\n"+walletModel.getDate()+"   "+walletModel.getTime());
                walletViewHolder.amount.setText("+ ₹"+walletModel.getAmount());
                walletViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(PaymentActivity.this, "transaction id"+walletModel.getTransaction_id(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new WalletViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.wallet_recycler_items,parent,false));
            }
        };
        recyclerwallet.setAdapter(walletViewHolderFirebaseRecyclerAdapter);
        walletViewHolderFirebaseRecyclerAdapter.startListening();

    }

    private void addMoneyToWallet() {
        price=addMoney.getText().toString().trim();

        if (TextUtils.isEmpty(price)){
            Toast.makeText(this, "Please Enter the required Amount", Toast.LENGTH_SHORT).show();
        }
        else {
            StartPayment(price);
        }
    }



    /////////////////////////////////////////////////////////
    private void StartPayment(final String amount) {
        dialog.show();
        final String M_ID="SwJHHy22345152915037";
        final String customer_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String order_id= UUID.randomUUID().toString().substring(0,28);
        final String URL ="https://daynee.000webhostapp.com/Paytm/generateChecksum.php";
        final String CallBAck_URL ="https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

        RequestQueue requestQueue= Volley.newRequestQueue(PaymentActivity.this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")){
                        String CHECKSUMHASH =jsonObject.getString("CHECKSUMHASH");

                        PaytmPGService paytmPGService =PaytmPGService.getStagingService("");

                        HashMap<String, String> paramMap = new HashMap<String,String>();
                        paramMap.put( "MID" , M_ID);
                        paramMap.put( "ORDER_ID" , order_id);
                        paramMap.put( "CUST_ID" , customer_id);
                        paramMap.put( "CHANNEL_ID" , "WAP");
                        paramMap.put( "TXN_AMOUNT" , amount);
                        paramMap.put( "WEBSITE" , "WEBSTAGING");
                        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                        paramMap.put( "CALLBACK_URL", CallBAck_URL);
                        paramMap.put( "CHECKSUMHASH", CHECKSUMHASH);
                        PaytmOrder paytmOrder=new PaytmOrder(paramMap);
                        paytmPGService.initialize(paytmOrder,null);
                        paytmPGService.startPaymentTransaction(PaymentActivity.this, true, true,
                                new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void onTransactionResponse(Bundle inResponse) {

                                        Toast.makeText(PaymentActivity.this, ""+inResponse.getString("RESPMSG"), Toast.LENGTH_SHORT).show();
                                        String response = inResponse.getString("RESPMSG");
                                        if (response.equals("Txn Success")) {
                                            walletRef= FirebaseDatabase.getInstance().getReference().child("wallet");

                                            HashMap<String,Object> hashMap=new HashMap<>();
                                            hashMap.put("customer_id",customer_id);
                                            hashMap.put("time",CurrentTime);
                                            hashMap.put("amount",amount);
                                            hashMap.put("type","add");
                                            hashMap.put("transaction_id",order_id);
                                            hashMap.put("date",CurrentDate);

                                            walletRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(order_id)
                                                    .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(), "Money Added To Wallet ", Toast.LENGTH_LONG).show();
                                                        changeBalance(amount);
                                                        dialog.dismiss();

                                                    }
                                                    dialog.dismiss();

                                                }
                                            });

                                        }

                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                        dialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        dialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        dialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();


                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                        dialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        dialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

                                    }
                                });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(PaymentActivity.this, "Something went Wrong!", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> paramMap = new HashMap<String,String>();
                paramMap.put( "MID" , M_ID);
                paramMap.put( "ORDER_ID" , order_id);
                paramMap.put( "CUST_ID" , customer_id);
                paramMap.put( "CHANNEL_ID" , "WAP");
                paramMap.put( "TXN_AMOUNT" , amount);
                paramMap.put( "WEBSITE" , "WEBSTAGING");
                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                paramMap.put( "CALLBACK_URL", CallBAck_URL);


                return paramMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void changeBalance(String value) {
        int updatedbalance= Integer.parseInt(currentBalance)+Integer.parseInt(value);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("wallet_balance",Integer.toString(updatedbalance));
        userRef.updateChildren(hashMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}