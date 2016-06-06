/*
 * This file is part of Movecraft.
 *
 *     Movecraft is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Movecraft is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Movecraft.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.countercraft.movecraft.utils;

import org.bukkit.Location;

public class MathUtils {

	public static boolean playerIsWithinBoundingPolygon( int[][][] box, int minX, int minZ, MovecraftLocation l ) {

		if ( l.x >= minX && l.x < ( minX + box.length ) ) {
			// PLayer is within correct X boundary
			if ( l.z >= minZ && l.z < ( minZ + box[l.x - minX].length ) ) {
				// Player is within valid Z boundary
				int minY, maxY;

				try {
					minY = box[l.x - minX][l.z - minZ][0];
					maxY = box[l.x - minX][l.z - minZ][1];
				} catch ( NullPointerException e ) {
					return false;
				}

				if ( l.y >= minY && l.y <= ( maxY + 2 ) ) {
					// Player is on board the vessel
					return true;
				}


			}

		}

		return false;
	}

	public static MovecraftLocation bukkit2MovecraftLoc( Location l ) {
		return new MovecraftLocation( l.getBlockX(), l.getBlockY(), l.getBlockZ() );
	}

	public static MovecraftLocation rotateVec( Rotation r, MovecraftLocation l ) {
		double theta;
		if ( r == Rotation.CLOCKWISE ) {
			theta = 0.5 * Math.PI;
		} else {
			theta = -1 * 0.5 * Math.PI;
		}

		int x = ( int ) Math.round( ( l.x * Math.cos( theta ) ) + ( l.z * ( -1 * Math.sin( theta ) ) ) );
		int z = ( int ) Math.round( ( l.x * Math.sin( theta ) ) + ( l.z * Math.cos( theta ) ) );

		return new MovecraftLocation(x, l.y, z);
	}

	public static double[] rotateVec( Rotation r, double x, double z ) {
		double theta;
		if ( r == Rotation.CLOCKWISE ) {
			theta = 0.5 * Math.PI;
		} else {
			theta = -1 * 0.5 * Math.PI;
		}

		double newX = Math.round( ( x * Math.cos( theta ) ) + ( z * ( -1 * Math.sin( theta ) ) ) );
		double newZ = Math.round( ( x * Math.sin( theta ) ) + ( z * Math.cos( theta ) ) );

		return new double[]{ newX, newZ };
	}

	public static double[] rotateVecNoRound( Rotation r, double x, double z ) {
		double theta;
		if ( r == Rotation.CLOCKWISE ) {
			theta = 0.5 * Math.PI;
		} else {
			theta = -1 * 0.5 * Math.PI;
		}

		double newX =  ( x * Math.cos( theta ) ) + ( z * ( -1 * Math.sin( theta ) ) ) ;
		double newZ =  ( x * Math.sin( theta ) ) + ( z * Math.cos( theta ) ) ;

		return new double[]{ newX, newZ };
	}

	public static int positiveMod( int mod, int divisor ) {
		if ( mod < 0 ) {
			mod += divisor;
		}

		return mod;
	}
}
