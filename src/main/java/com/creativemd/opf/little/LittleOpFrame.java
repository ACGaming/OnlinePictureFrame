package com.creativemd.opf.little;

import java.util.ArrayList;

import javax.annotation.Nullable;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.creativemd.creativecore.client.rendering.RenderCubeObject;
import com.creativemd.creativecore.client.rendering.RenderHelper3D;
import com.creativemd.creativecore.common.utils.CubeObject;
import com.creativemd.creativecore.gui.opener.GuiHandler;
import com.creativemd.littletiles.common.gui.handler.LittleGuiHandler;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.common.utils.LittleTileBlock;
import com.creativemd.littletiles.common.utils.LittleTilePreview;
import com.creativemd.littletiles.common.utils.LittleTileTileEntity;
import com.creativemd.littletiles.common.utils.small.LittleTileVec;
import com.creativemd.opf.block.TileEntityPicFrame;
import com.creativemd.opf.client.PicTileRenderer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleOpFrame extends LittleTileTileEntity {
	
	public LittleOpFrame()
	{
		super();
	}
	
	public LittleOpFrame(TileEntityPicFrame te, Block block, int meta)
	{
		super(block, meta, te);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<RenderCubeObject> getInternalRenderingCubes()
	{
		ArrayList<RenderCubeObject> cubes = new ArrayList<RenderCubeObject>();
		if(((TileEntityPicFrame) getTileEntity()).visibleFrame)
		{
			for (int i = 0; i < boundingBoxes.size(); i++) {
				CubeObject cube = boundingBoxes.get(i).getCube();
				EnumFacing direction = EnumFacing.getFront(meta);
				double width = 0.025;
				switch(direction)
				{
				case EAST:
					cube.maxX = (float) (cube.minX+width);
					break;
				case WEST:
					cube.minX = (float) (cube.maxX-width);
					break;
				case UP:
					cube.maxY = (float) (cube.minY+width);
					break;
				case DOWN:
					cube.minY = (float) (cube.maxY-width);
					break;
				case SOUTH:
					cube.maxZ = (float) (cube.minZ+width);
					break;
				case NORTH:
					cube.minZ = (float) (cube.maxZ-width);
					break;
				default:
					break;
				}
				cubes.add(new RenderCubeObject(cube, Blocks.PLANKS, 0));
			}
		}
		return cubes;
	}
	
	@Override
	public boolean canBeRenderCombined(LittleTile tile) {
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!super.onBlockActivated(worldIn, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ))
		{
			LittleGuiHandler.openGui("littleOpFrame", new NBTTagCompound(), player, this);
			return true;
		}
		return true;
	}
	
	@Override
	public boolean shouldTick()
	{
		return false;
	}
	
	@Override
	public ItemStack getDrop()
	{
		return new ItemStack(this.block, 1, meta);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
		if(getTileEntity() != null)
		{
			TileEntityPicFrame frame = (TileEntityPicFrame) getTileEntity();
			/*AxisAlignedBB bb = INFINITE_EXTENT_AABB;
	        return bb;*/
			float thickness = 0.05F;
			float offsetX = 0;
			if(frame.posX == 1)
				offsetX = -frame.sizeX/2F;
			else if(frame.posX == 2)
				offsetX = (float) (-frame.sizeX+gridMCLength);
			float offsetY = 0;
			if(frame.posY == 1)
				offsetY = -frame.sizeY/2F;
			else if(frame.posY == 2)
				offsetY = (float) (-frame.sizeY+gridMCLength);
			CubeObject cube = new CubeObject(0, offsetY, offsetX, thickness, frame.sizeY+offsetY, frame.sizeX+offsetX);
			EnumFacing direction = EnumFacing.getFront(meta);
			
			/*float sizeX = frame.sizeX;
			if(sizeX == 0)
				sizeX = 1;
			float sizeY = frame.sizeY;
			if(sizeY == 0)
				sizeY = 1;
			double offsetX = 0;
			double offsetY = 0;
			
			switch(frame.rotation)
			{
			case 1:
				sizeX = frame.sizeY;
				sizeY = -frame.sizeX;
				if(frame.posY == 0)
					offsetY += 1;
				else if(frame.posY == 2)
					offsetY -= 1;
				break;
			case 2:
				sizeX = -frame.sizeX;
				sizeY = -frame.sizeY;
				if(frame.posX == 0)
					offsetX += 1;
				else if(frame.posX == 2)
					offsetX -= 1;
				if(frame.posY == 0)
					offsetY += 1;
				else if(frame.posY == 2)
					offsetY -= 1;
				break;
			case 3:
				sizeX = -frame.sizeY;
				sizeY = frame.sizeX;
				if(frame.posX == 0)
					offsetX += 1;
				else if(frame.posX == 2)
					offsetX -= 1;
				break;
			}
			
			if(frame.posX == 1)
				offsetX += (-sizeX+1)/2D;
			else if(frame.posX == 2)
				offsetX += -sizeX+1;
			
			
			if(frame.posY == 1)
				offsetY += (-sizeY+1)/2D;
			else if(frame.posY == 2)
				offsetY += -sizeY+1;
			
			EnumFacing direction = EnumFacing.getFront(meta);
			if(direction == EnumFacing.UP)
			{
				cube.minZ -= sizeX-1;
				cube.minY -= sizeY-1;
				
				cube.minZ -= offsetX;
				cube.maxZ -= offsetX;
				cube.minY -= offsetY;
				cube.maxY -= offsetY;
			}else{
				cube.maxZ += sizeX-1;
				cube.maxY += sizeY-1;
				
				cube.minZ += offsetX;
				cube.maxZ += offsetX;
				cube.minY += offsetY;
				cube.maxY += offsetY;
			}
			
			cube = new CubeObject(Math.min(cube.minX, cube.maxX), Math.min(cube.minY, cube.maxY), Math.min(cube.minZ, cube.maxZ),
					Math.max(cube.minX, cube.maxX), Math.max(cube.minY, cube.maxY), Math.max(cube.minZ, cube.maxZ));*/
			
			Vector3f center = new Vector3f(thickness/2F, (float) LittleTile.gridMCLength/2F, (float) LittleTile.gridMCLength/2F);
			if(frame.rotation > 0)
			{
				Matrix3f rotation = new Matrix3f();
				rotation.rotX((float) Math.toRadians(frame.rotation*90F));
				cube.rotate(rotation, center);
			}
			
			if(direction.getAxis() != Axis.Y)
				cube.rotate(direction.rotateY(), center);
			else{
				Matrix3f rotation = new Matrix3f();
				if(direction == EnumFacing.UP)
					rotation.rotZ((float) Math.toRadians(90));
				else
					rotation.rotZ((float) Math.toRadians(-90));
				cube.rotate(rotation, center);
			}
	        return cube.getAxis().offset(cornerVec.getPosX(), cornerVec.getPosY(), cornerVec.getPosZ());
		}
		return super.getRenderBoundingBox();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderTileEntity(double x, double y, double z, float partialTickTime)
	{
		GlStateManager.pushMatrix();
		
		
		
		TileEntityPicFrame frame = (TileEntityPicFrame) getTileEntity();
		
		if(!frame.url.equals(""))
		{
			if(frame.isTextureLoaded())
			{
				float sizeX = frame.sizeX;
				float sizeY = frame.sizeY;
				GlStateManager.enableBlend();
	            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	            GlStateManager.disableLighting();
	            GlStateManager.color(1, 1, 1, 1);
	            GlStateManager.bindTexture(frame.textureID);
	            
	            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
	            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	            
	            GlStateManager.translate(-0.5, 0.5, 0.5);
	            
	            LittleTileVec position = cornerVec.copy();
	            EnumFacing direction = EnumFacing.getFront(meta);
	            if(direction.getAxisDirection() == AxisDirection.POSITIVE)
	            	position.addVec(new LittleTileVec(direction));
	            
	            switch (direction) {
				case SOUTH:
					position.x++;
					break;
				case WEST:
					position.z++;
					break;
				case UP:
					position.z++;
					break;
				default:
					break;
				}
	            
	            GlStateManager.translate(position.getPosX(), position.getPosY(), position.getPosZ());
	    		GlStateManager.pushMatrix();
	    		GlStateManager.translate(x, y, z);
	    		GlStateManager.translate(0.5, -0.5, -0.5);
	    		GlStateManager.pushMatrix();
	    		
	    		if(direction == EnumFacing.UP)
	    		{
	    			GL11.glRotated(90, 0, 0, 1);
	    			GlStateManager.translate(0, -gridMCLength, -gridMCLength);
	    		}
	    		else if(direction == EnumFacing.DOWN)
	    			GL11.glRotated(-90, 0, 0, 1);
	    		else
	    			RenderHelper3D.applyDirection(direction);
	    		
	    		//if((frame.rotation == 1 || frame.rotation == 3) && (frame.posX == 2 ^ frame.posY == 2))
	    			//GL11.glRotated(180, 1, 0, 0);
	    		
	    		GlStateManager.translate(0.001, gridMCLength/2, gridMCLength/2);
	    		
	    		GL11.glRotated(frame.rotation * 90, 1, 0, 0);
	    		//GL11.glRotated(System.nanoTime()/10000000D, 1, 0, 0);
	    		
	    		GL11.glRotated(frame.rotationX, 0, 1, 0);
	    		GL11.glRotated(frame.rotationY, 0, 0, 1);
	    		
	    		GlStateManager.translate(-0.5, 0.5+(frame.sizeY-1)/2-gridMCLength/2, 0.5+(frame.sizeX-1)/2-gridMCLength/2);
	    		
	    		double posX = 0;
	    		if(frame.posX == 1)
	    			posX = -sizeX/2;
	    		else if(frame.posX == 2)
	    			posX = -sizeX+gridMCLength;
	    		double posY = 0;
	    		if(frame.posY == 1)
	    			posY = -sizeY/2;
	    		else if(frame.posY == 2)
	    			posY = -sizeY+gridMCLength;
	    		
	    		GL11.glTranslated(0, posY, posX);
	    		
	    		
	    		
	    		GlStateManager.enableRescaleNormal();
	    		GL11.glScaled(1, frame.sizeY, frame.sizeX);
	    		
	    		GL11.glBegin(GL11.GL_POLYGON);
	    		GL11.glNormal3f(1.0f, 0.0F, 0.0f);
	    		
	    		GL11.glTexCoord3f(frame.flippedY ? 0 : 1, frame.flippedX ? 0 : 1, 0);
	    		GL11.glVertex3f(0.5F, -0.5f, -0.5f);
	    		GL11.glTexCoord3f(frame.flippedY ? 0 : 1, frame.flippedX ? 1 : 0, 0);
	    		GL11.glVertex3f(0.5f, 0.5f, -0.5f);
	    		GL11.glTexCoord3f(frame.flippedY ? 1 : 0, frame.flippedX ? 1 : 0, 0);
	    		GL11.glVertex3f(0.5f, 0.5f, 0.5f);
	    		GL11.glTexCoord3f(frame.flippedY ? 1 : 0, frame.flippedX ? 0 : 1, 0);
	    		GL11.glVertex3f(0.5f, -0.5f, 0.5f);
	    		GL11.glEnd();
	    		
	    		GlStateManager.popMatrix();
	    		GlStateManager.popMatrix();
	    		
	    		GlStateManager.disableRescaleNormal();
	            GlStateManager.disableBlend();
	            GlStateManager.enableLighting();
			}else{
				frame.loadTexutre();
			}
		}
		GlStateManager.popMatrix();
	}
	
	@Override
	public LittleTilePreview getPreviewTile() {
		NBTTagCompound nbt = new NBTTagCompound();
		saveTileExtra(nbt);
		nbt.setString("tID", getID());		
		return new LittlePlacedOpFrame(boundingBoxes.get(0).copy(), nbt);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void receivePacket(NBTTagCompound nbt, NetworkManager net)
	{
		super.receivePacket(nbt, net);
		te.updateRenderBoundingBox();
		te.updateRenderDistance();
	}
	
	@Override
	public boolean needCustomRendering()
	{
		return true;
	}

}
