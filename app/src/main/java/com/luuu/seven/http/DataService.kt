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
    @GET("")
    fun getIndexData(): Observable<List<IndexBean>>

    //漫画更新 num: 100 全部漫画, 1 原创漫画, 0 译制漫画
    @GET("")
    fun getAllUpdataComic(@Path("num") num: Int, @Path("id") id: Int): Observable<List<ComicUpdateBean>>

    //漫画排行 type: 0 人气排行, 1 吐槽排行, 2 订阅排行
    @GET("")
    fun getRankComic(@Path("type") type: Int, @Path("id") id: Int): Observable<List<HotComicBean>>

    //专题
    @GET("")
    fun getSpecialComic(@Path("id") id: Int): Observable<List<ComicSpecialBean>>

    //专题详情
    @GET("")
    fun getSpecialComicDetail(@Path("objId") objId: Int): Observable<ComicSpecialDetBean>

    //漫画排行 人气榜 待删除
    @GET("")
    fun getHotComic(@Path("id") id: Int): Observable<List<HotComicBean>>

    //漫画分类
    @GET("")
    fun getSortComic(): Observable<List<ComicSortBean>>

    //漫画分类里面对应的漫画列表
    @GET("")
    fun getSortComicList(@Path("sortid") sortid: Int, @Path("page") page: Int): Observable<List<ComicSortListBean>>

    //新闻里面的大图轮播
    @GET("")
    fun getComicNewsPic(): Observable<ComicNewsPicBean>

    //新闻
    @GET("")
    fun getComicNewsList(@Path("id") id: Int): Observable<List<ComicNewsListBean>>

    //新闻
    @GET("")
    fun getComicNewsFlash(@Path("id") id: Int): Observable<List<ComicNewsFlashBean>>

    //漫画介绍以及章节
    @GET("")
    fun getComicIntro(@Path("comicid") comicid: Int): Observable<ComicIntroBean>

    //漫画阅读
    @GET("")
    fun getComicReadPage(@Path("comicid") comicid: Int, @Path("chapterid") chapterid: Int): Observable<ComicReadBean>

    //漫画搜索
    @GET("")
    fun getSearchData(@Path("keyname") keyname: String): Observable<List<SearchDataBean>>

    @GET("")
    fun getHotSearch(): Observable<List<HotSearchBean>>
}