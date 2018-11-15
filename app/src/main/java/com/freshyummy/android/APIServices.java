package com.freshyummy.android;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServices {
    @FormUrlEncoded
    @POST("signin.php")
    Call<GetValue<User>> signin(@Field("xkey") String xkey,
                                    @Field("email") String email,
                                    @Field("nohp") String nohp,
                                    @Field("token") String token);

    @FormUrlEncoded
    @POST("signup.php")
    Call<SetValue> signup(@Field("xkey") String xkey,
                                    @Field("email") String email,
                                    @Field("telp") String telp,
                                    @Field("nama") String nama,
                                    @Field("token") String token);

    @FormUrlEncoded
    @POST("signupfb.php")
    Call<SetValue> signupfb(@Field("xkey") String xkey,
                          @Field("email") String email,
                          @Field("telp") String telp,
                          @Field("nama") String nama,
                            @Field("idfacebook") String idfacebook,
                          @Field("token") String token);

    @FormUrlEncoded
    @POST("getallproductcategory.php")
    Call<GetValue<Category>> getallproductcategory(@Field("xkey") String xkey);

    @FormUrlEncoded
    @POST("getactivead.php")
    Call<GetValue<Ad>> getactivead(@Field("xkey") String xkey);

    @FormUrlEncoded
    @POST("getproductcategory.php")
    Call<GetValue<Product>> getproductcategory(@Field("xkey") String xkey,
                                          @Field("kategori") String kategori);

    @FormUrlEncoded
    @POST("getproductcategoryother.php")
    Call<GetValue<Product>> getproductcategoryother(@Field("xkey") String xkey,
                                               @Field("kategori") String kategori,
                                                @Field("namaproduk") String namaproduk);

    @FormUrlEncoded
    @POST("getallproductdiscount.php")
    Call<GetValue<Product>> getallproductdiscount(@Field("xkey") String xkey);

    @FormUrlEncoded
    @POST("getnewestproduct.php")
    Call<GetValue<Product>> getnewestproduct(@Field("xkey") String xkey);


    @FormUrlEncoded
    @POST("getusercart.php")
    Call<GetValue<Cart>> getusercart(@Field("xkey") String xkey,
                                    @Field("iddetailproduk") String iddetailproduk);

    @FormUrlEncoded
    @POST("getbank.php")
    Call<GetValue<Bank>> getbank(@Field("xkey") String xkey);

    @FormUrlEncoded
    @POST("getongkir.php")
    Call<SetValue> getongkir(@Field("xkey") String xkey,
                             @Field("latitude") String latitude,
                             @Field("longitude") String longitude);

//    @FormUrlEncoded
//    @POST("getphone.php")
//    Call<SetValue> getphone(@Field("xkey") String xkey,
//                            @Field("email") String email);

    @FormUrlEncoded
    @POST("setusertransaction.php")
    Call<SetValue> setusertransaction(@Field("xkey") String xkey,
                                     @Field("idpelanggan") String idPelanggan,
                                     @Field("penerimapengantaran") String penerimapengantaran,
                                     @Field("alamatpengantaran") String alamatpengantaran,
                                     @Field("latitude") String latitude,
                                     @Field("longitude") String longitude,
                                     @Field("telepon") String telepon,
                                     @Field("keteranganalamat") String keteranganalamat,
                                     @Field("keteranganbarang") String keteranganbarang,
                                     @Field("jenisbayar") String jenisbayar,
                                     @Field("tanggalpengantaran") String tanggalpengantaran,
                                     @Field("ongkir") String ongkir,
                                     @Field("poinkeluar") String poinkeluar,
                                     @Field("poinmasuk") String poinmasuk,
                                      @Field("idpromo") String idpromo);

    @FormUrlEncoded
    @POST("setusertransactiondetail.php")
    Call<SetValue> setusertransactiondetail(@Field("xkey") String xkey,
                                           @Field("idtransaksi") String idtransaksi,
                                           @Field("iddetailproduk") String iddetailproduk,
                                           @Field("jumlahbeli") String jumlahbeli,
                                           @Field("hargabeli") String hargabeli);

    @FormUrlEncoded
    @POST("gettransactioncount.php")
    Call<SetValue> gettransactioncount(@Field("xkey") String xkey,
                                        @Field("idpelanggan") String idpelanggan);

    @FormUrlEncoded
    @POST("getusertransaction.php")
    Call<GetValue<Belanjaan>> getusertransaction(@Field("xkey") String xkey,
                                       @Field("idpelanggan") String idpelanggan);

    @FormUrlEncoded
    @POST("getusertransactiondetail.php")
    Call<GetValue<Belanjaan>> getusertransactiondetail(@Field("xkey") String xkey,
                                                 @Field("idtransaksi") String idtransaksi);

    @FormUrlEncoded
    @POST("getpromo.php")
    Call<GetValue<Promo>> getpromo(@Field("xkey") String xkey,
                                   @Field("idpelanggan") String idpelanggan);

    @FormUrlEncoded
    @POST("canceltransaction.php")
    Call<SetValue> canceltransaksi(@Field("xkey") String xkey,
                                   @Field("idtransaksi") String idtransaksi);

    @FormUrlEncoded
    @POST("transferconfirmation.php")
    Call<SetValue> transferconfirmation(@Field("xkey") String xkey,
                                   @Field("idtransaksi") String idtransaksi,
                                        @Field("gambar") String gambar,
                                        @Field("namabank") String namabank,
                                        @Field("namarekening") String namarekening,
                                        @Field("norekening") String norekening,
                                        @Field("tanggal") String tanggal,
                                        @Field("jumlahtransfer") String jumlahtransfer);

    @FormUrlEncoded
    @POST("getpoin.php")
    Call<SetValue> getpoin(@Field("xkey") String xkey,
                                   @Field("idpelanggan") String idpelanggan);

    @FormUrlEncoded
    @POST("searchproduct.php")
    Call<GetValue<Product>> searchproduct(@Field("xkey") String xkey,
                           @Field("searchkey") String searchkey);

    @FormUrlEncoded
    @POST("updateakun.php")
    Call<SetValue> updateakun(@Field("xkey") String xkey,
                           @Field("idpelanggan") String idpelanggan,
                              @Field("nama") String nama,
                              @Field("email") String email,
                              @Field("telepon") String telepon);

    @FormUrlEncoded
    @POST("getpoindetail.php")
    Call<GetValue<PoinDetail>> getpoindetail(@Field("xkey") String xkey,
                                             @Field("idpelanggan") String idpelanggan);

    @FormUrlEncoded
    @POST("checkfacebook.php")
    Call<GetValue<User>> checkfacebook(@Field("xkey") String xkey,
                                  @Field("idfacebook") String idfacebook,
                                       @Field("email") String email,
                                       @Field("token") String token);

    @FormUrlEncoded
    @POST("updateidfacebook.php")
    Call<SetValue> updateidfacebook(@Field("xkey") String xkey,
                                       @Field("idfacebook") String idfacebook,
                                       @Field("email") String email,
                                       @Field("token") String token);

    @FormUrlEncoded
    @POST("connectfacebook.php")
    Call<SetValue> connectfacebook(@Field("xkey") String xkey,
                                    @Field("idfacebook") String idfacebook,
                                    @Field("idpelanggan") String idpelanggan);

    @FormUrlEncoded
    @POST("tracking.php")
    Call<GetValue<Tracking>> tracking(@Field("xkey") String xkey,
                                       @Field("idtransaksi") String idtransaksi);
}