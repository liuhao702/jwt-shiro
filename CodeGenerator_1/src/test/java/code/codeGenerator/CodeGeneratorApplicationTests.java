package code.codeGenerator;

import code.entity.CesEntity;
import code.entity.JsonEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.lang.reflect.Type;
import java.util.*;

@SpringBootTest
class CodeGeneratorApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;
    @Autowired
	private CesEntity cesEntity;

	@Test  //新特性
	void contextLoads() {
		List<String> list = new ArrayList<String>();
			list.add("c");
			list.add("a");
			list.add("d");
			list.add("b");
			list.add("e");
		Collections.sort(list,(a, b)->a.compareTo(b));
		list.forEach((s) -> System.err.println(s));
		list.forEach(System.out::println);
	}

	/**
	 * 存对象到redis
	 */
	@Test
	void testSet() {
		cesEntity.setValue("你好");
		redisTemplate.opsForValue().set("ces",cesEntity);
		System.err.println(redisTemplate.opsForValue().get("ces"));
	}
	/**
	 * 获取redis存在的值
	 */
	@Test
	void testGet() {
		cesEntity = (CesEntity)redisTemplate.opsForValue().get("ces");
		System.err.println(cesEntity.toString());
	}
	/**
	 * 删除redis存在的值
	 */
	@Test
	void testDel() {
		redisTemplate.delete("Zset");
		List<String> ls= new ArrayList<>();
		ls.add("runoob");
		ls.add("bxmadmin");
		ls.add("myKey");
		ls.add("Zset1");
		ls.add("Zset");
		ls.add("a");
		ls.add("list");
		redisTemplate.delete(ls); //批量删除
		boolean b = redisTemplate.hasKey("ces");
		System.err.println(cesEntity.toString());
	}

	/**
	 * 存字符串存储
	 */
	@Test
	void testRedisString() {
		redisTemplate.opsForValue().set("Str","testRedisString!");
		System.err.println(redisTemplate.opsForValue().get("Str"));
	}

	/**
	 * redis list有序列表集合存储
	 */
	@Test
	void testRedisList() {
		 ListOperations<String,String> list= redisTemplate.opsForList();
		list.leftPush("list","a");
		list.leftPush("list","b");
		list.leftPush("list","c");
		List<String> range = list.range("list", 0, list.size("list"));
		System.err.println(range);
	}
	/**
	 * redis set无序集合存储
	 */
	@Test
	void testRedisSet() {
		SetOperations<String,String> set= redisTemplate.opsForSet();
		set.add("a","qqq");
		set.add("a","www");
		set.add("a","www");
		set.add("c","eee");
		Set<String> sets = set.members("a");
		System.err.println(sets);
	}
	/**
	 * redis map存储
	 */
	@Test
	void testRedisMap() {
		HashOperations<String,String,String> map= redisTemplate.opsForHash();
		map.put("kk","a","s");
		System.err.println(map.get("kk","a"));
	}

	/**
	 * redis Zset有序存储
	 */
	@Test
	void testRedisZset() {
		ZSetOperations<String,String> zSetOperations= redisTemplate.opsForZSet();
		zSetOperations.add("Zset","aa1",1);
		zSetOperations.add("Zset","aa2",2);
		zSetOperations.add("Zset1","aa3",3);
		Set<String> sets = zSetOperations.range("Zset",0,2);
		System.err.println(sets);
	}


	/**
	 * Gson对象序列化/反序列化
	 */
	@Test
	void testGson() {
		JsonEntity ces= new JsonEntity();
		ces.setValue("天天向上");
		ces.setId(1L);
		ces.setAge(18);
		ces.setName("留的话");
		Gson gson = new Gson();
		//gson序列化
		String cesStr  =gson.toJson(ces);
		System.err.println(cesStr);
		String str = "{'id':2,'value':'好好学习'}";
		//gson反序列化
		JsonEntity cesJson = gson.fromJson(str,JsonEntity.class);
		System.err.println(cesJson);
	}

	/**
	 * Gson对象包含对象序列化/反序列化
	 */
	@Test
	void testGson1() {
		JsonEntity ces= new JsonEntity();
		ces.setValue("天天向上");
		ces.setId(1L);
		ces.setAge(18);
		ces.setName("留的话");
		CesEntity cesEntity = new CesEntity();
		cesEntity.setId(10L);
		cesEntity.setValue("你哈");
		ces.setCesEntity(cesEntity);
		Gson gson = new Gson();
		//gson序列化
		String cesStr  =gson.toJson(ces);
		System.err.println(cesStr);
		String str = "{'id':1,'value':'天天向上','name':'留的话','age':18,'cesEntity':{'id':10,'value':'你哈'}}";
		//gson反序列化
		JsonEntity cesJson = gson.fromJson(str,JsonEntity.class);
		System.err.println(cesJson);
	}
	/**
	 * Gson Array 和 List 的序列化/反序列化
	 */
	@Test
	void testGson2() {
		//Array
		String namesJson = "['xiaoqiang','chenrenxiang','hahaha']";
		Gson gson = new Gson();
		//反序列化
		String[] nameArray = gson.fromJson(namesJson, String[].class);
		//序列化
		System.err.println(gson.toJson(nameArray));
		for (String s : nameArray) {
			//System.err.println(s);
		}
		//List
		String userJson = "[{'id':10,'value':'你哈','cesEntity':{'id':13,'value':'大王'}},{'id':12,'value':'糖果'}]";
		Type userListType = new TypeToken<ArrayList<CesEntity>>(){}.getType();
		List<CesEntity> userList = gson.fromJson(userJson, userListType);
		userList.forEach(System.err::println);
	}

	/**
	 * Gson Map 的序列化/反序列化
	 */
	@Test
	void testGson3() {
		//序列化 如果设置值为null但是序列化出来的值不会带值为这个属性
		JsonEntity ces= new JsonEntity();
		ces.setValue("天天向上");
		ces.setId(1L);
		ces.setAge(18);
		ces.setName(null);
		Gson gson = new Gson();
		String userJson = gson.toJson(ces);
		System.err.println(userJson);
		//反序列化 如果没有传的参数将会变成null
		String json = "{'id':13,'value':'大王'}";
		Type map = new TypeToken<HashMap<String,Object>>(){}.getType();
		Map<String,Object> mapS = gson.fromJson(json, map);
		System.err.println(mapS);
	}



	/**
	 * Gson 变量值为null时的序列化/反序列化
	 */
	@Test
	void testGson4() {
		//序列化 如果设置值为null但是序列化出来的值不会带值为这个属性
		JsonEntity ces= new JsonEntity();
		ces.setValue("天天向上");
		ces.setId(1L);
		ces.setAge(18);
		ces.setName(null);
		Gson gson = new Gson();
		String userJson = gson.toJson(ces);
		System.err.println(userJson);
		//反序列化 如果没有传的参数将会变成null
		String json = "{'id':13,'value':'大王'}";
		CesEntity user = gson.fromJson(userJson, CesEntity.class);
		System.err.println(user);
	}

}
