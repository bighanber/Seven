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
    suspend fun getIndexData(): List<IndexBean>

    //漫画更新 num: 100 全部漫画, 1 原创漫画, 0 译制漫画
    @GET("")
    suspend fun getAllUpdataComic(@Path("num") num: Int, @Path("id") id: Int): List<ComicUpdateBean>

    //漫画排行 type: 0 人气排行, 1 吐槽排行, 2 订阅排行
    @GET("")
    suspend fun getRankComic(@Path("type") type: Int, @Path("id") id: Int): List<HotComicBean>

    //专题
    @GET("")
    fun getSpecialComic(@Path("id") id: Int): Observable<List<ComicSpecialBean>>

    //专题详情
    @GET("")
    fun getSpecialComicDetail(@Path("objId") objId: Int): Observable<ComicSpecialDetBean>

    //漫画排行 人气榜 待删除
    @GET("")
    fun getHotComic(@Path("id") id: Int): Observable<List<HotComicBean>>

    //漫画分类筛选
    @GET("")
    suspend fun getSortComicFilter(): List<SortFilterBean>

    //漫画分类里面对应的漫画列表
    @GET("")
    suspend fun getSortComicList(@Path("filter") filter: String, @Path("page") page: Int): List<ComicSortListBean>

    //新闻里面的大图轮播
    @GET("")
    suspend fun getComicNewsPic(): ComicNewsPicBean

    //新闻
    @GET("")
    suspend fun getComicNewsList(@Path("id") id: Int): List<ComicNewsListBean>

    //新闻
    @GET("")
    suspend fun getComicNewsFlash(@Path("id") id: Int): List<ComicNewsFlashBean>

    //漫画介绍以及章节
    @GET("")
    suspend fun getComicIntro(@Path("comicid") comicid: Int): ComicIntroBean

    @GET("")
    suspend fun getComicRelated(@Path("comic_id") comicId: Int): ComicRelatedInfoBean

    //漫画阅读
    @GET("")
    suspend fun getComicReadPage(@Path("comicid") comicid: Int, @Path("chapterid") chapterid: Int): ComicReadBean

    //漫画搜索
    @GET("")
    suspend fun getSearchData(@Path("keyword") keyword: String): List<SearchDataBean>

    @GET("")
    suspend fun getHotSearch(): List<HotSearchBean>
}