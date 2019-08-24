package com.luuu.seven.http

import com.luuu.seven.bean.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:
 */
interface DataService {
    //漫画首页数据
    @GET("recommend.json")
    suspend fun getIndexData(): List<IndexBean>

    //漫画更新 num: 100 全部漫画, 1 原创漫画, 0 译制漫画
    @GET("latest/{num}/{id}.json")
    suspend fun getAllUpdataComic(@Path("num") num: Int, @Path("id") id: Int): List<ComicUpdateBean>

    //漫画排行 type: 0 人气排行, 1 吐槽排行, 2 订阅排行
    @GET("rank/0/0/{type}/{id}.json")
    suspend fun getRankComic(@Path("type") type: Int, @Path("id") id: Int): List<HotComicBean>

    //专题
    @GET("subject/0/{id}.json")
    fun getSpecialComic(@Path("id") id: Int): Observable<List<ComicSpecialBean>>

    //专题详情
    @GET("subject/{objId}.json")
    fun getSpecialComicDetail(@Path("objId") objId: Int): Observable<ComicSpecialDetBean>

    //漫画排行 人气榜 待删除
    @GET("rank/0/0/0/{id}.json")
    fun getHotComic(@Path("id") id: Int): Observable<List<HotComicBean>>

    //漫画分类
    @GET("0/category.json")
    fun getSortComic(): Observable<List<ComicSortBean>>

    //漫画分类里面对应的漫画列表
    @GET("classify/{sortid}/0/{page}.json")
    fun getSortComicList(@Path("sortid") sortid: Int, @Path("page") page: Int): Observable<List<ComicSortListBean>>

    //新闻里面的大图轮播
    @GET("article/recommend/header.json")
    fun getComicNewsPic(): Observable<ComicNewsPicBean>

    //新闻
    @GET("article/list/v2/0/2/{id}.json")
    fun getComicNewsList(@Path("id") id: Int): Observable<List<ComicNewsListBean>>

    //新闻
    @GET("message/list/{id}.json")
    fun getComicNewsFlash(@Path("id") id: Int): Observable<List<ComicNewsFlashBean>>

    //漫画介绍以及章节
    @GET("comic/{comicid}.json")
    suspend fun getComicIntro(@Path("comicid") comicid: Int): ComicIntroBean

    //漫画阅读
    @GET("chapter/{comicid}/{chapterid}.json")
    fun getComicReadPage(@Path("comicid") comicid: Int, @Path("chapterid") chapterid: Int): Observable<ComicReadBean>

    //漫画搜索
    @GET("search/show/0/{keyname}/0.json")
    fun getSearchData(@Path("keyname") keyname: String): Observable<List<SearchDataBean>>

    @GET("search/hot/0.json")
    fun getHotSearch(): Observable<List<HotSearchBean>>
}