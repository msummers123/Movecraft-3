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

public class BoundingBoxUtils {

	public static int[][][] formBoundingBox( MovecraftLocation[] blockList, Integer minX, Integer maxX, Integer minZ, Integer maxZ ) {
		int sizeX = ( maxX - minX ) + 1;
		int sizeZ = ( maxZ - minZ ) + 1;

		int[][][] polygonalBox = new int[sizeX][][];

		for ( MovecraftLocation l : blockList ) {
			if ( polygonalBox[l.x - minX] == null ) {
				polygonalBox[l.x - minX] = new int[sizeZ][];
			}

			int minY, maxY;

			if ( polygonalBox[l.x - minX][l.z - minZ] == null ) {

				polygonalBox[l.x - minX][l.z - minZ] = new int[2];
				polygonalBox[l.x - minX][l.z - minZ][0] = l.y;
				polygonalBox[l.x - minX][l.z - minZ][1] = l.y;

			} else {
				minY = polygonalBox[l.x - minX][l.z - minZ][0];
				maxY = polygonalBox[l.x - minX][l.z - minZ][1];

				if ( l.y < minY ) {
					polygonalBox[l.x - minX][l.z - minZ][0] = l.y;
				}
				if ( l.y > maxY ) {
					polygonalBox[l.x - minX][l.z - minZ][1] = l.y;
				}

			}
		}

		return polygonalBox;
	}

	public static int[][][] translateBoundingBoxVertically( int[][][] hitbox, int dy ) {
		int[][][] newHitbox = new int[hitbox.length][][];

		for ( int x = 0; x < hitbox.length; x++ ) {
			newHitbox[x] = new int[hitbox[x].length][];

			for ( int z = 0; z < hitbox[x].length; z++ ) {

				if(hitbox[x][z]!=null) {
					newHitbox[x][z] = new int[2];
					newHitbox[x][z][0] = hitbox[x][z][0] + dy;
					newHitbox[x][z][1] = hitbox[x][z][1] + dy;
				}

			}

		}


		return newHitbox;
	}

}
