package net.borisshoes.endernexus.cca;

import net.borisshoes.endernexus.Destination;
import net.minecraft.nbt.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DestinationComponent implements IDestinationComponent{
   
   public final ArrayList<Destination> destinations = new ArrayList<>();
   
   @Override
   public List<Destination> getDestinations(){
      return destinations;
   }
   
   @Override
   public boolean addDestination(Destination dest){
      if (destinations.contains(dest)) return false;
      return destinations.add(dest);
   }
   
   @Override
   public boolean removeDestination(Destination dest){
      if (!destinations.contains(dest)) return false;
      return destinations.remove(dest);
   }
   
   @Override
   public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup){
      try{
         destinations.clear();
         // TODO: handle optionals in a safer way?
         NbtList destsTag = tag.getListOrEmpty("Destinations");
         for (NbtElement e : destsTag) {
            NbtCompound destTag = (NbtCompound) e;
            NbtList posTag = destTag.getList("pos").orElseThrow();
            Vec3d pos = new Vec3d(posTag.getDouble(0).orElseThrow(), posTag.getDouble(1).orElseThrow(), posTag.getDouble(2).orElseThrow());
            NbtList rotTag = destTag.getList("rot").orElseThrow();
            Vec2f rot = new Vec2f(rotTag.getFloat(0).orElseThrow(), rotTag.getFloat(1).orElseThrow());
            destinations.add(new Destination(destTag.getString("name").orElseThrow(), pos, rot, destTag.getString("world").orElseThrow()));
         }
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   
   @Override
   public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup){
      try{
         NbtList destsTag = new NbtList();
         for(Destination dest : destinations){
            NbtCompound blockTag = new NbtCompound();
            NbtList pos = new NbtList();
            pos.add(0, NbtDouble.of(dest.getPos().getX()));
            pos.add(1, NbtDouble.of(dest.getPos().getY()));
            pos.add(2, NbtDouble.of(dest.getPos().getZ()));
            NbtList rot = new NbtList();
            rot.add(0, NbtFloat.of(dest.getRotation().x));
            rot.add(1, NbtFloat.of(dest.getRotation().y));
            blockTag.put("pos",pos);
            blockTag.put("rot",rot);
            blockTag.putString("name",dest.getName());
            blockTag.putString("world",dest.getWorldKey());
            destsTag.add(blockTag);
         }
         tag.put("Destinations",destsTag);
      }catch(Exception e){
         e.printStackTrace();
      }
   }
}
