package cn.hjf.tmcrmnew;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Customer {
	@PrimaryKey
	public String uuid;
	@ColumnInfo(name = "name")
	public String name;
	@ColumnInfo(name = "id_number")
	public String idNumber;
	@ColumnInfo(name = "id_card_image_list")
	public List<String> IdCardImageList;
}
